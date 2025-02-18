package com.sion.concertbooking.infrastructure.kafka;

import com.sion.concertbooking.intefaces.event.PaymentEventMessageConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class KafkaPaymentEventListener {

    private final PaymentEventMessageConsumer paymentEventMessageConsumer;

    public KafkaPaymentEventListener(PaymentEventMessageConsumer paymentEventMessageConsumer) {
        this.paymentEventMessageConsumer = paymentEventMessageConsumer;
    }

    @KafkaListener(
            containerFactory = "concurrentKafkaListenerContainerFactory",
            topics = "TEST",
            groupId = "group1"
    )
    public void paymentListener(List<String> messages, Acknowledgment acknowledgment) {
        paymentEventMessageConsumer.consume(messages);
        acknowledgment.acknowledge();
    }
}
