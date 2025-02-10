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
/**
 * @Value("${spring.data.redis.host}")
 *     private String redisHost;
 *
 *     @Value("${spring.data.redis.port}")
 *     private int redisPort;
 *
 *     @Value("${spring.data.redis.password}")
 *     private String redisPassword;
 *
 *     @Bean
 *     public RedisConnectionFactory redisConnectionFactoryForDev () {
 *         RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
 *         redisStandaloneConfiguration.setHostName(redisHost);
 *         redisStandaloneConfiguration.setPort(redisPort);
 *         redisStandaloneConfiguration.setPassword(redisPassword);
 *         return new LettuceConnectionFactory(redisStandaloneConfiguration);
 *     }
 */