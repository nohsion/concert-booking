package com.sion.concertbooking.intefaces.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sion.concertbooking.domain.event.EventOutboxCommand;
import com.sion.concertbooking.domain.event.EventOutboxInfo;
import com.sion.concertbooking.domain.event.EventOutboxService;
import com.sion.concertbooking.domain.event.EventOutboxUtils;
import com.sion.concertbooking.domain.payment.PaymentRequestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KafkaPaymentEventConsumer {

    private static final TypeReference<List<PaymentRequestEvent>> PAYMENT_REQUEST_EVENT_TYPE = new TypeReference<>() {
    };

    private final EventOutboxService eventOutboxService;

    public KafkaPaymentEventConsumer(EventOutboxService eventOutboxService) {
        this.eventOutboxService = eventOutboxService;
    }

    @KafkaListener(
            containerFactory = "concurrentKafkaListenerContainerFactory",
            topics = "payment",
            groupId = "payment-consumer"
    )
    public void paymentListener(List<String> messages, Acknowledgment acknowledgment) throws JsonProcessingException {
        List<PaymentRequestEvent> events = EventOutboxUtils.OBJECT_MAPPER.readValue(
                messages.toString(), PAYMENT_REQUEST_EVENT_TYPE);
        events.stream()
                .map(event -> eventOutboxService.getEventOutBoxes(EventOutboxCommand.fromEvent(event)))
                .flatMap(List::stream)
                .map(EventOutboxInfo::eventId)
                .collect(Collectors.toSet())
                .forEach(eventOutboxService::done);
        acknowledgment.acknowledge();
    }
}
