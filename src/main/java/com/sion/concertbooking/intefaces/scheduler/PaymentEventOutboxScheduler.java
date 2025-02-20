package com.sion.concertbooking.intefaces.scheduler;

import com.sion.concertbooking.domain.event.EventOutbox;
import com.sion.concertbooking.domain.event.EventOutboxService;
import com.sion.concertbooking.domain.payment.PaymentEventMessage;
import com.sion.concertbooking.domain.payment.PaymentEventSender;
import com.sion.concertbooking.domain.payment.PaymentRequestEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentEventOutboxScheduler {

    private final EventOutboxService eventOutboxService;
    private final PaymentEventSender paymentEventSender;

    public PaymentEventOutboxScheduler(
            EventOutboxService eventOutboxService,
            PaymentEventSender paymentEventSender
    ) {
        this.eventOutboxService = eventOutboxService;
        this.paymentEventSender = paymentEventSender;
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void retryPaymentRequestEventOutbox() {
        List<EventOutbox> pendingOutboxes = eventOutboxService.getPendingOutboxes(
                PaymentRequestEvent.AGGEREGATE_TYPE,
                PaymentRequestEvent.EVENT_TYPE
        );
        pendingOutboxes.forEach(eventOutbox ->
                paymentEventSender.send(PaymentEventMessage.fromEventOutbox(eventOutbox)));
    }
}
