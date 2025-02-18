package com.sion.concertbooking.infrastructure.kafka;

import com.sion.concertbooking.domain.payment.PaymentEventMessage;
import com.sion.concertbooking.domain.payment.PaymentEventSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class KafkaPaymentEventSender implements PaymentEventSender {

    private final KafkaTemplate<String, String> producer;
    
    public KafkaPaymentEventSender(KafkaTemplate<String, String> producer) {
        this.producer = producer;
    }

    @Override
    public void send(final PaymentEventMessage message) {
        CompletableFuture<SendResult<String, String>> future = producer.send(message.topic(), message.payload());
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                throw new KafkaException("Failed to send message to Kafka. message=" + message, ex);
            } else {
                log.debug("Sent message to Kafka. message={}, result={}", message, result.getRecordMetadata());
            }
        });
    }
}
