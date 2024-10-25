package com.example.lectureserver.event.ticket;

public record TicketEvent (
        Long ticketId,
        String email
) {
}
