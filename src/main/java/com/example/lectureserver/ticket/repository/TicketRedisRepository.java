package com.example.lectureserver.ticket.repository;

import com.example.lectureserver.ticket.controller.dto.RedisResult;
import com.example.lectureserver.ticket.controller.dto.RedisVO;
import com.example.lectureserver.ticket.service.RedisOperation;
import com.example.lectureserver.ticket.service.RedisTransaction;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTransaction redisTransaction;
    private final RedisOperation<RedisVO> operation;

    public RedisResult execute(RedisVO redisVO) {
        List<Object> execute = redisTransaction.execute(redisTemplate, operation, redisVO);
        List<Long> result = new ArrayList<>();
        for (Object o : execute) {
            result.add((Long) o);
        }
        return new RedisResult(result.get(0), result.get(1) == 1L);
    }

    public Long increment() {
        return redisTemplate
                .opsForValue()
                .increment("ticket_count");
    }

    public Long addKeyToSet(String value) {
        return redisTemplate.opsForSet()
                .add("applied-user", value);
    }

    public Long getSetCount(String key) {
        return redisTemplate.opsForSet()
                .size(key);
    }

    public Long addToSet(String key, String email) {
        return redisTemplate.opsForSet()
                .add(key, email);
    }
}
