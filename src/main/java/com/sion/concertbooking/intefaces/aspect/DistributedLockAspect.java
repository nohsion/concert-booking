package com.sion.concertbooking.intefaces.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Component
public class DistributedLockAspect {

    private static final String LOCK_PREFIX = "lock:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    public DistributedLockAspect(RedissonClient redissonClient, AopForTransaction aopForTransaction) {
        this.redissonClient = redissonClient;
        this.aopForTransaction = aopForTransaction;
    }

    @Around("@annotation(DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String lockKey = LOCK_PREFIX + distributedLock.keyPrefix() + CustomSpringELParser.getDynamicValue(
                signature.getParameterNames(),
                joinPoint.getArgs(),
                distributedLock.key()
        );
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean acquired = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!acquired) {
                return false;
            }
            return aopForTransaction.proceed(joinPoint);
        } finally {
            lock.unlock();
        }
    }

    @Around("@annotation(DistributedMultiLock)")
    public Object multiLock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        DistributedMultiLock distributedMultiLock = method.getAnnotation(DistributedMultiLock.class);

        Object[] dynamicValues = CustomSpringELParser.getDynamicValues(
                signature.getParameterNames(),
                joinPoint.getArgs(),
                distributedMultiLock.keys()
        );
        List<String> lockKeyNames = Arrays.stream(dynamicValues)
                .map(String::valueOf)
                .map(key -> LOCK_PREFIX + distributedMultiLock.keyPrefix() + key)
                .toList();

        RLock multiLock = redissonClient.getMultiLock(
                lockKeyNames.stream()
                        .map(redissonClient::getLock)
                        .toArray(RLock[]::new)
        );

        try {
            boolean acquired = multiLock.tryLock(distributedMultiLock.waitTime(), distributedMultiLock.leaseTime(), distributedMultiLock.timeUnit());
            if (!acquired) {
                return false;
            }
            return aopForTransaction.proceed(joinPoint);
        } finally {
            multiLock.unlock();
        }
    }
}
