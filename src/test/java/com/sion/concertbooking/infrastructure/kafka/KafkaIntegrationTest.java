package com.sion.concertbooking.infrastructure.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class KafkaIntegrationTest {

    private static final String TEST_TOPIC = "test-topic";

    @Autowired
    private KafkaTemplate<String, String> producer;

    private String receivedMessage;

    @KafkaListener(
            containerFactory = "concurrentKafkaListenerContainerFactory",
            topics = TEST_TOPIC,
            groupId = "test-consumer"
    )
    void paymentListener(List<String> messages, Acknowledgment acknowledgment) {
        receivedMessage = messages.get(0);
        acknowledgment.acknowledge();
    }

    @Test
    void producerAndConsumerTest() {
        String payload = "{\"orderId\":1,\"amount\":10000}";

        producer.send(TEST_TOPIC, payload);

        await()
                .pollInterval(Duration.ofMillis(300)) // 언제마다 한번씩 = 지금은 300ms 에 1번씩
                .atMost(10, TimeUnit.SECONDS) // 최대 10초까지
                .untilAsserted(
                        () -> assertThat(receivedMessage).isEqualTo(payload)
                );

    }


}
