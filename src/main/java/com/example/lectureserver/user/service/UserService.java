package com.example.lectureserver.user.service;

import com.example.lectureserver.user.controller.dto.LoginRequest;
import com.example.lectureserver.user.controller.dto.UserResponse;
import com.example.lectureserver.user.controller.dto.JoinRequest;
import com.example.lectureserver.user.manager.UserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserManager userManager;

    public UserResponse join(JoinRequest joinRequest) {
        return new UserResponse(userManager.join(joinRequest).getId());
    }

    public UserResponse login(LoginRequest loginRequest) {
        return new UserResponse(userManager.login(loginRequest.email(), loginRequest.password()).getId());
    }

}
