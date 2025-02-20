package com.sion.concertbooking.intefaces.event;

import com.sion.concertbooking.domain.event.EventOutboxService;
import com.sion.concertbooking.domain.payment.PaymentRequestEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PaymentRequestEventOutboxListener {

    private final EventOutboxService eventOutboxService;

    public PaymentRequestEventOutboxListener(EventOutboxService eventOutboxService) {
        this.eventOutboxService = eventOutboxService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleBeforeCommit(final PaymentRequestEvent paymentEvent) {
        eventOutboxService.save(paymentEvent.toEventOutbox());
    }
}
