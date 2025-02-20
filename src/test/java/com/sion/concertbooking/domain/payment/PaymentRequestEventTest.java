package com.sion.concertbooking.domain.payment;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentRequestEventTest {

    @Test
    void paymentRequestToPayload() {
        PaymentRequestEvent paymentRequestEvent = Instancio.of(PaymentRequestEvent.class)
                .create();
        String payload = paymentRequestEvent.toPayload();

        assertThat(payload)
                .as("Event 클래스의 필드는 payload에 포함되지 않아야 한다.")
                .doesNotContain(
                        "topic",
                        "deadLetterTopic",
                        "eventType",
                        "aggregateId"
                )
                .as("PaymentRequestEvent의 필드는 payload에 포함되어야 한다.")
                .contains(
                        "tokenId",
                        "concertId",
                        "userId",
                        "totalPrice",
                        "reservations"
                );
    }
}