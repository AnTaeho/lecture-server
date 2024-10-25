package com.example.lectureserver.ticket.controller.dto;

public record TicketRequest(
        Long lectureId,
        int amount
) {
}
