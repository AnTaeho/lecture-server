package com.example.lectureserver.lecture.service;

import com.example.lectureserver.aop.lock.DistributedLock;
import com.example.lectureserver.lecture.controller.dto.LectureDetailResponse;
import com.example.lectureserver.lecture.controller.dto.LectureListResponse;
import com.example.lectureserver.lecture.controller.dto.LectureRegisterRequest;
import com.example.lectureserver.lecture.controller.dto.LectureResponse;
import com.example.lectureserver.lecture.controller.dto.LectureSimpleResponse;
import com.example.lectureserver.lecture.controller.dto.ReservationRequest;
import com.example.lectureserver.lecture.controller.dto.ReservationResult;
import com.example.lectureserver.lecture.domain.Lecture;
import com.example.lectureserver.lecture.repository.LectureRepository;
import com.example.lectureserver.payment.dto.PaymentResult;
import com.example.lectureserver.payment.service.PaymentService;
import com.example.lectureserver.reservation.service.ReservationService;
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
    private final UserRepository userRepository;

    private final PaymentService paymentService;
    private final ReservationService reservationService;

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

    @DistributedLock(key = "'LECTURE_LOCK_' + #lectureId")
    public ReservationResult reserveLecture(ReservationRequest request, String email, Long lectureId) {
        Lecture lecture = getLecture(lectureId);
        User user = getUser(email);

        PaymentResult paymentResult = paymentService.payPrice(user.getId(), lecture, request.seatNumbers());

        reservationService.reserveSeat(email, lectureId, request.seatNumbers());

        return new ReservationResult(paymentResult.totalPrice(), lectureId);
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
