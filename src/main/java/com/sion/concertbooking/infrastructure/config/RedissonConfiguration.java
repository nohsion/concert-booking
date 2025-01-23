package com.sion.concertbooking.infrastructure.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfiguration {

    private static final String PROTOCOL = "redis://";

    @Value("${redisson.host}")
    private String host;
    @Value("${redisson.port}")
    private int port;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(PROTOCOL + host + ":" + port);
        return Redisson.create(config);
    }
}
