package com.sion.concertbooking.test;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
class RedissonTestcontainersConfiguration {

    public static final GenericContainer<?> REDIS_CONTAINER;

    static {
        REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:7.4.2"))
                .withExposedPorts(6379)
                .withReuse(true);
        REDIS_CONTAINER.start();

        System.setProperty("redisson.host", REDIS_CONTAINER.getHost());
        System.setProperty("redisson.port", String.valueOf(REDIS_CONTAINER.getMappedPort(6379)));
    }

    @PreDestroy
    public void preDestroy() {
        if (REDIS_CONTAINER.isRunning()) {
            REDIS_CONTAINER.stop();
        }
    }
}