package com.sion.concertbooking.domain.payment;

public record PaymentEventMessage(
        String topic,
        String payload
) {
    public static PaymentEventMessage from(PaymentRequestEvent event) {
        return new PaymentEventMessage(
                "payment",
                event.toPayload()
        );
    }

    public static PaymentEventMessage toDeadLetterTopic(PaymentRequestEvent event) {
        return new PaymentEventMessage(
                "payment.DLT",
                event.toPayload()
        );
    }
}
