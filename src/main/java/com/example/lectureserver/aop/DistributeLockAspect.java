package com.example.lectureserver.aop;

import com.example.lectureserver.common.annotation.DistributedLock;
import java.lang.reflect.Method;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class DistributeLockAspect {

    private final RedisSimpleLock redisSimpleLock;

    @Around("@annotation(com.example.lectureserver.common.annotation.DistributedLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String lockKey = distributedLock.key();
        String lockValue = UUID.randomUUID().toString();

        try {
            boolean acquired = redisSimpleLock.tryLock(
                    lockKey,
                    lockValue,
                    distributedLock.leaseTime(),
                    distributedLock.timeUnit()
            );
            if (!acquired) {
                throw new IllegalArgumentException("lock exception");
            }
            return joinPoint.proceed();
        } finally {
            redisSimpleLock.releaseLock(lockKey, lockValue);
        }
    }

}
