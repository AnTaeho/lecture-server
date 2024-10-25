package com.example.lectureserver.kafka.consumer;

import com.example.lectureserver.event.ticket.TicketEvent;
import com.example.lectureserver.ticket.domain.Ticket;
import com.example.lectureserver.ticket.domain.TicketOutbox;
import com.example.lectureserver.ticket.domain.UserTicket;
import com.example.lectureserver.ticket.repository.TicketOutboxRepository;
import com.example.lectureserver.ticket.repository.TicketRepository;
import com.example.lectureserver.ticket.repository.UserTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CouponCreateConsumer {

    private final UserTicketRepository userTicketRepository;
    private final TicketOutboxRepository ticketOutboxRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    @KafkaListener(topics = "issue-ticket", groupId = "group_1")
    public void listener(TicketEvent ticketEvent) {
        log.info("ticketId = {} email = {}", ticketEvent.ticketId(), ticketEvent.email());
        userTicketRepository.save(new UserTicket(ticketEvent.ticketId(), ticketEvent.email()));
        decreaseTicketAmount(ticketEvent);
        updateOutboxStatus(ticketEvent);
    }

    private void decreaseTicketAmount(TicketEvent ticketEvent) {
        Ticket ticket = ticketRepository.findById(ticketEvent.ticketId())
                .orElseThrow(() -> new IllegalArgumentException("해당 티켓은 없습니다."));
        ticket.issue();
    }

    private void updateOutboxStatus(TicketEvent ticketEvent) {
        TicketOutbox ticketOutbox = ticketOutboxRepository.findByTicketIdAndEmail(ticketEvent.ticketId(), ticketEvent.email())
                .orElseThrow(() -> new IllegalArgumentException("해당 기록을 찾을 수 없습니다."));
        ticketOutbox.done();
    }

}
