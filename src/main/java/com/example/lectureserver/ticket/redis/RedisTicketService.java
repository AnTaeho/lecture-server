package com.example.lectureserver.ticket.redis;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisTicketService {

    private static final String TICKET_COUNT = "ticket_count:";

    private final RedisTemplate<String, String> redisTemplate;

    public void increment(Long ticketId) {
        redisTemplate
                .opsForValue()
                .increment(makeTicketCount(ticketId));
    }

    public Long addKeyToSet(String value) {
        return redisTemplate
                .opsForSet()
                .add("applied-user", value);
    }

    public boolean isNotServed(Long ticketId) {
        return redisTemplate
                .opsForValue()
                .get(makeTicketCount(ticketId)) == null;
    }

    public Long decrease(Long ticketId) {
        return redisTemplate
                .opsForValue()
                .decrement(makeTicketCount(ticketId));
    }

    public void setTicketCount(Long ticketId, int amount) {
        redisTemplate
                .opsForValue()
                .set(makeTicketCount(ticketId), String.valueOf(amount));
    }

    private String makeTicketCount(Long ticketId) {
        return TICKET_COUNT + ticketId;
    }

    public boolean checkUserChargeTrial(String email) {
        if (redisTemplate.opsForValue().get(email) != null) {
            return true;
        }

        redisTemplate.opsForValue().set(email, "LOCK", 10, TimeUnit.SECONDS);

        return false;
    }

    public Long getAmount(Long ticketId) {
        String result = redisTemplate.opsForValue()
                .get("ticketId:" + ticketId);
        if (result == null) {
            return null;
        }
        return Long.parseLong(result);
    }
}
