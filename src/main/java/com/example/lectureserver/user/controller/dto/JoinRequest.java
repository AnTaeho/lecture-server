package com.example.lectureserver.user.controller.dto;

public record JoinRequest (
        String username,
        String email,
        String password
) {
}
