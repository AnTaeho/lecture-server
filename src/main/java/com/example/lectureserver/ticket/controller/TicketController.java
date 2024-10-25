package com.example.lectureserver.ticket.controller;

import com.example.lectureserver.ticket.controller.dto.TicketRequest;
import com.example.lectureserver.ticket.controller.dto.TicketResponse;
import com.example.lectureserver.ticket.service.TicketService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ticket")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(ticketService.createTicket(ticketRequest));
    }

    @PostMapping("/{ticketId}")
    public ResponseEntity<Void> issueTicket(@PathVariable("ticketId") Long ticketId) {
        String email = UUID.randomUUID().toString();
        ticketService.issueTicket(ticketId, email);
        return ResponseEntity.ok(null);
    }

}
