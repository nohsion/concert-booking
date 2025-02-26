package com.sion.concertbooking.test;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * https://java.testcontainers.org/modules/kafka/
 */
@Configuration
public class KafkaTestcontainersConfiguration {

    public static final ConfluentKafkaContainer KAFKA_CONTAINER;

    static {
        KAFKA_CONTAINER = new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.8.1"));
        KAFKA_CONTAINER.start();

        System.setProperty("spring.kafka.bootstrap-servers", KAFKA_CONTAINER.getBootstrapServers());
        System.setProperty("spring.kafka.consumer.bootstrap-servers", KAFKA_CONTAINER.getBootstrapServers());
        System.setProperty("spring.kafka.producer.bootstrap-servers", KAFKA_CONTAINER.getBootstrapServers());
    }

    @PreDestroy
    public void preDestroy() {
        if (KAFKA_CONTAINER.isRunning()) {
            KAFKA_CONTAINER.stop();
        }
    }
}
