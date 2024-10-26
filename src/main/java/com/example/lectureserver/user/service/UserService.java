package com.example.lectureserver.user.service;

import com.example.lectureserver.balance.domain.Balance;
import com.example.lectureserver.balance.repository.BalanceRepository;
import com.example.lectureserver.user.controller.dto.LoginRequest;
import com.example.lectureserver.user.controller.dto.UserResponse;
import com.example.lectureserver.user.controller.dto.JoinRequest;
import com.example.lectureserver.user.domain.User;
import com.example.lectureserver.user.manager.UserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserManager userManager;
    private final BalanceRepository balanceRepository;

    public UserResponse join(JoinRequest joinRequest) {
        return new UserResponse(userManager.join(joinRequest).getId());
    }

    public UserResponse login(LoginRequest loginRequest) {
        return new UserResponse(userManager.login(loginRequest.email(), loginRequest.password()).getId());
    }

    @Transactional
    public void chargeBalance(String email, int amount) {
        User user = userManager.getUser(email);
        Balance balance = balanceRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 계좌를 찾을 수 없습니다."));

        balance.charge(amount);
    }

}
