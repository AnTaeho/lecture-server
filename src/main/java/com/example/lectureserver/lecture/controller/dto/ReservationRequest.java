package com.example.lectureserver.lecture.controller.dto;

import java.util.List;

public record ReservationRequest(
        List<Integer> seatNumbers
) {
}
