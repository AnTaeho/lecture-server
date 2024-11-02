package com.example.lectureserver.ticket.service;

import com.example.lectureserver.event.ticket.TicketEvent;
import com.example.lectureserver.ticket.redis.RedisTicketService;
import com.example.lectureserver.ticket.controller.dto.TicketRequest;
import com.example.lectureserver.ticket.controller.dto.TicketResponse;
import com.example.lectureserver.ticket.manager.TickerCommandManager;
import com.example.lectureserver.ticket.manager.TicketQueryManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketQueryManager ticketQueryManager;
    private final TickerCommandManager tickerCommandManager;
    private final RedisTicketService redisTicketService;
    private final ApplicationEventPublisher publisher;

    public TicketResponse createTicket(TicketRequest ticketRequest) {
        return new TicketResponse(tickerCommandManager.createTicket(ticketRequest).getId());
    }

    @Transactional
    public void issueTicket(Long ticketId, String email) {
        Long add = redisTicketService.addKeyToSet(ticketId + ":" + email);
        if (add != 1L) {
            return;
        }

        if (redisTicketService.isNotServed(ticketId)) {
            int amount = ticketQueryManager.getAmount(ticketId);
            redisTicketService.setTicketCount(ticketId, amount);
        }

        Long decrease = redisTicketService.decrease(ticketId);

        if (decrease < 0L) {
//            redisTicketService.increment(ticketId);
            return;
        }

        publisher.publishEvent(new TicketEvent(ticketId, email));
    }
}


