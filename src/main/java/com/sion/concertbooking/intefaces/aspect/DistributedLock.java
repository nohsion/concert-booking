package com.sion.concertbooking.intefaces.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    String key();

    String keyPrefix() default "";

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 락 획득을 위해 기다리는 시간
     */
    long waitTime() default 5L;

    /**
     * 락 획득 이후, leaseTime 이 지나면 락을 해제한다
     */
    long leaseTime() default 3L;
}
