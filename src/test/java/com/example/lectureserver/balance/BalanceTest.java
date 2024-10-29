package com.example.lectureserver.balance;

import com.example.lectureserver.balance.domain.Balance;
import com.example.lectureserver.balance.repository.BalanceRepository;
import com.example.lectureserver.user.controller.dto.JoinRequest;
import com.example.lectureserver.user.controller.dto.UserResponse;
import com.example.lectureserver.user.repository.UserRepository;
import com.example.lectureserver.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BalanceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BalanceRepository balanceRepository;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @AfterEach
    void afterEach() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        balanceRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("10번의 연속 충전 요청이 있어도 1번만 성공한다.")
    void balanceTest() {
        UserResponse join = userService.join(new JoinRequest("username", "email", "password"));

        for (int i = 0; i < 10; i++) {
            userService.chargeBalance("email", 5000);
        }

        Balance balance = balanceRepository.findByUserId(join.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 계좌를 찾을 수 없습니다."));
        Assertions.assertThat(balance.getAmount()).isEqualTo(5000);
    }

    @Test
    @Transactional
    @DisplayName("10초안에 연속 충전 요청이 있어도 1번만 성공한다.")
    void balanceInTenSecondsTest() throws InterruptedException {
        UserResponse join = userService.join(new JoinRequest("username", "email", "password"));

        userService.chargeBalance("email", 5000);
        Thread.sleep(5000);
        userService.chargeBalance("email", 5000);

        Balance balance = balanceRepository.findByUserId(join.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 계좌를 찾을 수 없습니다."));
        Assertions.assertThat(balance.getAmount()).isEqualTo(5000);
    }

    @Test
    @Transactional
    @DisplayName("10초밖에 연속 충전 요청이 있으면 성공한다.")
    void balanceOutTenSecondsTest() throws InterruptedException {
        UserResponse join = userService.join(new JoinRequest("username", "email", "password"));

        userService.chargeBalance("email", 5000);
        Thread.sleep(10001);
        userService.chargeBalance("email", 5000);

        Balance balance = balanceRepository.findByUserId(join.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 계좌를 찾을 수 없습니다."));
        Assertions.assertThat(balance.getAmount()).isEqualTo(10000);
    }


}
