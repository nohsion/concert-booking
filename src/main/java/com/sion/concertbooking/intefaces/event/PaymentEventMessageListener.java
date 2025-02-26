package com.sion.concertbooking.intefaces.event;

import com.sion.concertbooking.domain.payment.PaymentEventMessage;
import com.sion.concertbooking.domain.payment.PaymentEventSender;
import com.sion.concertbooking.domain.payment.PaymentRequestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class PaymentEventMessageListener {

    private final PaymentEventSender sender;

    public PaymentEventMessageListener(PaymentEventSender sender) {
        this.sender = sender;
    }

    @Async("applicationEventExecutor")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAfterCommit(final PaymentRequestEvent event) {
        sender.send(PaymentEventMessage.from(event));
    }

    @Recover
    public void recoverPaymentEvent(Exception e, PaymentRequestEvent event) {
        log.error("3번의 재시도 후에도 메시지 전송이 실패하여 Dead Letter Topic으로 이동합니다. event={}", event, e);
        sender.send(PaymentEventMessage.toDeadLetterTopic(event));
    }
}
