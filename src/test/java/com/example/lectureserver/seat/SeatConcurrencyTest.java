package com.example.lectureserver.seat;

import static org.assertj.core.api.Assertions.*;

import com.example.lectureserver.balance.domain.Balance;
import com.example.lectureserver.balance.repository.BalanceRepository;
import com.example.lectureserver.lecture.controller.dto.LectureRegisterRequest;
import com.example.lectureserver.lecture.controller.dto.LectureResponse;
import com.example.lectureserver.lecture.controller.dto.ReservationRequest;
import com.example.lectureserver.lecture.domain.Lecture;
import com.example.lectureserver.lecture.repository.LectureRepository;
import com.example.lectureserver.lecture.service.LectureService;
import com.example.lectureserver.reservation.domain.Reservation;
import com.example.lectureserver.reservation.repository.ReservationRepository;
import com.example.lectureserver.seat.repository.SeatRepository;
import com.example.lectureserver.user.domain.User;
import com.example.lectureserver.user.repository.UserRepository;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class SeatConcurrencyTest {

    @Autowired
    LectureService lectureService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BalanceRepository balanceRepository;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    ReservationRepository reservationRepository;

    @BeforeEach
    @Transactional
    void beforeEach() {
        for (int i = 0; i < 10; i++) {
            User savedUser = userRepository.save(new User("name" + i, "email" + i, "password" + i));
            Balance savedBalance = balanceRepository.save(new Balance(savedUser));
            savedBalance.charge(1000000);
            balanceRepository.saveAndFlush(savedBalance);
        }
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        balanceRepository.deleteAll();
        lectureRepository.deleteAll();
        seatRepository.deleteAll();
        reservationRepository.deleteAll();
    }

    @Test
    @DisplayName("하나의 강의 대한 모든 좌석 예매 요청을 1000명이 동시에 하면 1명만 예매된다.")
    void seatTest() throws InterruptedException {
        LectureResponse lectureResponse = lectureService.registerLecture(new LectureRegisterRequest(
                "title",
                "description",
                "me",
                10,
                1
        ));
        Lecture lecture = lectureRepository.findById(lectureResponse.lectureId())
                .orElseThrow(IllegalStateException::new);

        // when
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        ReservationRequest req = new ReservationRequest(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        Long id = lecture.getId();
        for (int i = 0; i < threadCount; i++) {
            String email = "email" + i;
            executorService.submit(() -> {
                try {
                    lectureService.reserveLecture(req, email, id);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(9);

        executorService.shutdown();
    }

    @Test
    @DisplayName("좌석 예매 테스트")
    void seatTest2() {
        LectureResponse lectureResponse = lectureService.registerLecture(new LectureRegisterRequest(
                "title",
                "description",
                "me",
                10,
                1
        ));
        Lecture lecture = lectureRepository.findById(lectureResponse.lectureId())
                .orElseThrow(IllegalStateException::new);

        ReservationRequest req = new ReservationRequest(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        lectureService.reserveLecture(req, "email" + 1, lecture.getId());

        // then
        List<Reservation> all = reservationRepository.findAll();
        assertThat(all.size()).isEqualTo(10);
        for (Reservation reservation : all) {
            assertThat(reservation.getEmail()).isEqualTo("email" + 1);
            assertThat(reservation.getLectureId()).isEqualTo(lectureResponse.lectureId());
        }
        User user = userRepository.findByEmail("email" + 1).orElseThrow();
        Balance balance = balanceRepository.findByUserId(user.getId()).orElseThrow();
        assertThat(balance.getAmount()).isEqualTo(1000000 - 10);
    }

}
