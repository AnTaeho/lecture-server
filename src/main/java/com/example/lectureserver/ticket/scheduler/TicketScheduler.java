package com.example.lectureserver.ticket.scheduler;

import com.example.lectureserver.event.ticket.TicketEvent;
import com.example.lectureserver.kafka.producer.TickerMessageProducer;
import com.example.lectureserver.ticket.domain.TicketOutbox;
import com.example.lectureserver.ticket.manager.TicketOutboxManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketScheduler {

    private final TicketOutboxManager ticketOutboxManager;
    private final TickerMessageProducer tickerMessageProducer;

    @Scheduled(fixedRate = 300000)
    public void resendFailedMessage() {
        List<TicketOutbox> allPublished = ticketOutboxManager.findAllCreated();
        for (TicketOutbox ticketOutbox : allPublished) {
            tickerMessageProducer.create(
                    new TicketEvent(ticketOutbox.getTicketId(), ticketOutbox.getEmail())
            );
        }
    }

    @Scheduled(fixedRate = 300000)
    public void retryFailMessage() {
        List<TicketOutbox> allPublished = ticketOutboxManager.findAllPublished();

    }

    @Scheduled(fixedRate = 3600000)
    public void deleteOldMessage() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<Long> outboxOneHourAgo = ticketOutboxManager.findOutboxOneHourAgo(oneHourAgo);
        ticketOutboxManager.deleteAllIn(outboxOneHourAgo);
    }

}
