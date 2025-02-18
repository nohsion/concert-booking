package com.sion.concertbooking.intefaces.event;

import com.sion.concertbooking.domain.event.EventOutboxRecorder;
import com.sion.concertbooking.domain.payment.PaymentRequestEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PaymentRequestEventOutboxListener {

    private final EventOutboxRecorder eventOutboxRecorder;

    public PaymentRequestEventOutboxListener(EventOutboxRecorder eventOutboxRecorder) {
        this.eventOutboxRecorder = eventOutboxRecorder;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleBeforeCommit(final PaymentRequestEvent paymentEvent) {
        eventOutboxRecorder.save(paymentEvent.toEventOutbox());
    }
}
