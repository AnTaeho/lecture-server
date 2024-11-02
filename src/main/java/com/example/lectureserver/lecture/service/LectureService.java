package com.example.lectureserver.lecture.service;

import com.example.lectureserver.aop.lock.DistributedLock;
import com.example.lectureserver.balance.domain.Balance;
import com.example.lectureserver.balance.repository.BalanceRepository;
import com.example.lectureserver.lecture.controller.dto.LectureDetailResponse;
import com.example.lectureserver.lecture.controller.dto.LectureListResponse;
import com.example.lectureserver.lecture.controller.dto.LectureRegisterRequest;
import com.example.lectureserver.lecture.controller.dto.LectureResponse;
import com.example.lectureserver.lecture.controller.dto.LectureSimpleResponse;
import com.example.lectureserver.lecture.controller.dto.ReservationRequest;
import com.example.lectureserver.lecture.domain.Lecture;
import com.example.lectureserver.lecture.repository.LectureRepository;
import com.example.lectureserver.payment.domain.Payment;
import com.example.lectureserver.payment.repository.PaymentRepository;
import com.example.lectureserver.reservation.domain.Reservation;
import com.example.lectureserver.reservation.repository.ReservationRepository;
import com.example.lectureserver.seat.domain.Seat;
import com.example.lectureserver.seat.repository.SeatRepository;
import com.example.lectureserver.user.domain.User;
import com.example.lectureserver.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public LectureResponse registerLecture(LectureRegisterRequest lectureRegisterRequest) {
        Lecture lecture = new Lecture(
                lectureRegisterRequest.title(),
                lectureRegisterRequest.description(),
                lectureRegisterRequest.lecturer(),
                lectureRegisterRequest.size(),
                lectureRegisterRequest.price()
        );

        Lecture savedLecture = lectureRepository.save(lecture);

        for (int i = 1; i <= savedLecture.getSize(); i++) {
            seatRepository.save(new Seat(i, savedLecture));
        }

        return new LectureResponse(savedLecture.getId());
    }

    @DistributedLock(key = "LECTURE_LOCK")
    @Transactional
    public LectureResponse reserveLecture(ReservationRequest request, String email, Long lectureId) {
        Lecture lecture = getLecture(lectureId);
        User user = getUser(email);

        Balance balance = balanceRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));

        checkBalance(balance.getAmount(), lecture, request.seatNumbers().size());

        for (Integer seatNumber : request.seatNumbers()) {
            Seat seat = seatRepository.findSeatWithPessimisticLock(lectureId, seatNumber)
                    .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다."));
            seat.updateStatus();

            balance.use(lecture.getPrice());
            paymentRepository.save(new Payment(lecture.getPrice(), user.getId(), lecture.getId(), seatNumber));

            Reservation reservation = new Reservation(user.getEmail(), lecture.getId(), seatNumber);
            reservationRepository.save(reservation);
        }

        return new LectureResponse(lectureId);
    }

    private void checkBalance(int amount, Lecture lecture, int size) {
        if (amount < lecture.getPrice() * size) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
    }

    public LectureListResponse getALlLecture() {
        List<LectureSimpleResponse> result = lectureRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
        return new LectureListResponse(result);
    }

    public LectureDetailResponse getLectureDetail(Long lectureId) {
        Lecture lecture = getLecture(lectureId);
        return new LectureDetailResponse(
                lecture.getTitle(),
                lecture.getDescription(),
                lecture.getLecturer(),
                lecture.getSize()
        );
    }

    private Lecture getLecture(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다."));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(""));
    }

    private LectureSimpleResponse toResponse(Lecture lecture) {
        return new LectureSimpleResponse(
                lecture.getTitle(),
                lecture.getLecturer(),
                lecture.getSize()
        );
    }

}
