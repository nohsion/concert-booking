package com.sion.concertbooking.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfiguration {

    @Bean("postProcessExecutor")
    public Executor postProcessExecutor(
            @Value("${post-process.threads}") int numOfThreads
    ) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(numOfThreads);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setThreadNamePrefix("post-");
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean("applicationEventExecutor")
    public Executor applicationEventExecutor(
            @Value("${application-event.threads}") int numOfThreads
    ) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(numOfThreads);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setThreadNamePrefix("application-event-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
