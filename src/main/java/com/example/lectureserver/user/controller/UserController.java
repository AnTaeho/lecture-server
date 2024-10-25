package com.example.lectureserver.user.controller;

import com.example.lectureserver.user.controller.dto.LoginRequest;
import com.example.lectureserver.user.controller.dto.UserResponse;
import com.example.lectureserver.user.controller.dto.JoinRequest;
import com.example.lectureserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> join(@RequestBody JoinRequest joinRequest) {
        return ResponseEntity.ok(userService.join(joinRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }
}
