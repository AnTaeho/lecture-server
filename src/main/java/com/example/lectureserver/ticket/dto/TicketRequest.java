package com.example.lectureserver.ticket.dto;

public record TicketRequest(
        Long lectureId,
        int amount
) {
}
