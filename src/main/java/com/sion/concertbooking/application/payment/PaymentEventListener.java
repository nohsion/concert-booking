package com.sion.concertbooking.application.payment;

import com.sion.concertbooking.domain.dataplatform.DataPlatformClient;
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
public class PaymentEventListener {

    private final DataPlatformClient dataPlatformClient;

    public PaymentEventListener(DataPlatformClient dataPlatformClient) {
        this.dataPlatformClient = dataPlatformClient;
    }

    @Async("applicationEventExecutor")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentEvent(PaymentEvent event) {
        dataPlatformClient.sendReservationPayment(event.userId(), event.totalPrice(), event.reservations());
    }

    @Recover
    public void recoverPaymentEvent(Exception e, PaymentEvent event) {
        // TODO: Dead Letter Queue로 메시지를 전송하고, DLQ 모니터링을 통해 재처리를 수행해야 한다.
        log.error("3번의 재시도에도 PaymentEvent 후처리가 실패하여, DLQ로 메시지를 전송합니다. event: {}", event, e);
    }
}
