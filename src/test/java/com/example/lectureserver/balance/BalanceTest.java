package com.example.lectureserver.balance;

import com.example.lectureserver.balance.domain.Balance;
import com.example.lectureserver.balance.repository.BalanceRepository;
import com.example.lectureserver.user.controller.dto.JoinRequest;
import com.example.lectureserver.user.controller.dto.UserResponse;
import com.example.lectureserver.user.repository.UserRepository;
import com.example.lectureserver.user.service.UserService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BalanceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BalanceRepository balanceRepository;

    @Test
    @Transactional
    @DisplayName("1000번의 동시 충전 요청이 있어도 1번만 성공한다.")
    void balanceTest() {
        UserResponse join = userService.join(new JoinRequest("username", "email", "password"));

        userService.chargeBalance("email", 5000);

        Balance balance = balanceRepository.findByUserId(join.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 계좌를 찾을 수 없습니다."));
        Assertions.assertThat(balance.getAmount()).isEqualTo(5000);


    }

    @Test
    @Transactional
    @DisplayName("1000번의 동시 충전 요청이 있어도 1번만 성공한다.")
    void balanceConcurrencyTest() throws InterruptedException {
        UserResponse join = userService.join(new JoinRequest("username", "email@email.com", "password"));

        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userService.chargeBalance("email@email.com", 5000);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long stopTime = System.currentTimeMillis();
        System.out.println(stopTime - startTime);

        Balance balance = balanceRepository.findByUserId(join.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 계좌를 찾을 수 없습니다."));
        Assertions.assertThat(balance.getAmount()).isEqualTo(5000);


    }

}
