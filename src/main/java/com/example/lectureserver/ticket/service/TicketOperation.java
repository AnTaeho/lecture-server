package com.example.lectureserver.ticket.service;

import com.example.lectureserver.ticket.controller.dto.RedisVO;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Component;

@Component
public class TicketOperation implements RedisOperation<RedisVO> {

    @Override
    public Long count(RedisOperations<String, Object> operations, RedisVO redisVO) {
        return operations.opsForSet()
                .size(redisVO.key());
    }

    @Override
    public Long add(RedisOperations<String, Object> operations, RedisVO redisVO) {
        return operations.opsForSet()
                .add(redisVO.key(), redisVO.value());
    }

    @Override
    public void execute(RedisOperations callbackOperations, Object vo) {
        this.count(callbackOperations, (RedisVO) vo);
        this.add(callbackOperations, (RedisVO) vo);
    }

}
