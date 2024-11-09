package com.example.lectureserver.ticket.controller.dto;

public record RedisResult(
        Long size,
        boolean alreadyIn
) {
}
