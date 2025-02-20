package com.sion.concertbooking.domain.payment;

public record PaymentEventMessage(
        String topic,
        String payload
) {
    public static PaymentEventMessage from(PaymentRequestEvent event) {
        return new PaymentEventMessage(
                event.getAggregateType(), // aggregateType(ex. payment)을 kafka topic으로 사용
                event.toPayload()
        );
    }

    public static PaymentEventMessage toDeadLetterTopic(PaymentRequestEvent event) {
        return new PaymentEventMessage(
                event.getAggregateType() + ".DLT",
                event.toPayload()
        );
    }
}
