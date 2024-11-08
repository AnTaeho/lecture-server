package com.example.lectureserver.ticket.manager;

import com.example.lectureserver.event.ticket.TicketEvent;
import com.example.lectureserver.ticket.domain.TicketOutbox;
import com.example.lectureserver.ticket.repository.TicketOutboxRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class TicketOutboxManager {

    private final TicketOutboxRepository ticketOutboxRepository;

    public void save(TicketOutbox ticketOutbox) {
        ticketOutboxRepository.save(ticketOutbox);
    }

    public List<TicketOutbox> findAllCreated() {
        return ticketOutboxRepository.findAllNotSendingMessage();
    }

    public List<TicketOutbox> findAllPublished() {
        return ticketOutboxRepository.findAllPublished();
    }

    public List<Long> findOutboxOneHourAgo(LocalDateTime oneHourAgo) {
        return ticketOutboxRepository.findOutboxOneHourAgo(oneHourAgo);
    }

    public void deleteAllIn(List<Long> outboxOneHourAgo) {
        ticketOutboxRepository.deleteAllIn(outboxOneHourAgo);
    }

    public void updateOutboxStatus(TicketEvent ticketEvent) {
        ticketOutboxRepository.updateToPublished(ticketEvent.ticketId(), ticketEvent.email());
    }
}
