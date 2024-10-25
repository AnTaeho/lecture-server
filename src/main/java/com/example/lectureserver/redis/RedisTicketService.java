package com.example.lectureserver.redis;

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
}
