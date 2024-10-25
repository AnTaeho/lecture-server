package com.example.lectureserver.event.ticket;

import com.example.lectureserver.producer.TickerMessageProducer;
import com.example.lectureserver.ticket.domain.TicketOutbox;
import com.example.lectureserver.ticket.manager.TicketOutboxManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class TicketEventListener {

    private final TicketOutboxManager ticketOutboxManager;
    private final TickerMessageProducer tickerMessageProducer;

    @TransactionalEventListener(value = TicketEvent.class, phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutboxInfo(TicketEvent ticketEvent) {
        log.info("Save outbox info");
        ticketOutboxManager.save(new TicketOutbox(ticketEvent.ticketId(), ticketEvent.email()));
    }

    @Async
    @TransactionalEventListener(value = TicketEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void produceMessage(TicketEvent ticketEvent) {
        log.info("Produce message");
        tickerMessageProducer.create(ticketEvent);
    }

}
