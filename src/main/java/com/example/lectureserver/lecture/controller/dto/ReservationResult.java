package com.example.lectureserver.lecture.controller.dto;

public record ReservationResult(
        int totalPrice,
        Long lectureId
) {
}
