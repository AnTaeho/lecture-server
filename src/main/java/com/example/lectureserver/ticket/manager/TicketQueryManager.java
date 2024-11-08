package com.example.lectureserver.ticket.manager;

import com.example.lectureserver.ticket.domain.Ticket;
import com.example.lectureserver.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TicketQueryManager {

    private final TicketRepository ticketRepository;

    public int getAmount(Long ticketId) {
        return ticketRepository.getTicketAmount(ticketId);
    }

    public Ticket getTicket(Long ticketId) {
        return ticketRepository.findByIdWithLock(ticketId)
                .orElseThrow();

    }
}
