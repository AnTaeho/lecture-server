package com.example.lectureserver.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisTicketService {

    private final RedisTemplate<String, String> redisTemplate;

    public Long increment() {
        return redisTemplate
                .opsForValue()
                .increment("ticket_count");
    }

    public Long addKeyToSet(String value) {
        return redisTemplate.opsForSet()
                .add("applied-user", value);
    }
}
