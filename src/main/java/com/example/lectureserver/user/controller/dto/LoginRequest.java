package com.example.lectureserver.user.controller.dto;

public record LoginRequest (
        String email,
        String password
) {
}
