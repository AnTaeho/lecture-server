package com.example.lectureserver.aop;

import com.example.lectureserver.common.annotation.DistributedLock;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class DistributeLockAspect {

    private final RedissonClient redisson;

    @Around("@annotation(com.example.lectureserver.common.annotation.DistributedLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String email = (String) joinPoint.getArgs()[0];
        String lockKey = distributedLock.key();

        RLock lock = redisson.getLock(lockKey + email);
        try {
            boolean acquired = lock.tryLock(
                    distributedLock.waitTime(),
                    distributedLock.leaseTime(),
                    distributedLock.timeUnit()
            );
            log.info("get lock");
            if (!acquired) {
                throw new IllegalArgumentException();
            }
            return joinPoint.proceed();
        } finally {
            lock.unlock();
            log.info("lock released");
        }
    }

}
