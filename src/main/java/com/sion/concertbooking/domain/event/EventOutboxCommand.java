package com.sion.concertbooking.domain.event;

import com.sion.concertbooking.domain.payment.PaymentRequestEvent;

/**
 * 카프카 메시지 이벤트별로 fromEvent()를 통해 EventOutbox용 Command로 변환
 */
public record EventOutboxCommand(
        long aggregateId,
        String aggregateType,
        String eventType
) {

    public static EventOutboxCommand fromEvent(PaymentRequestEvent event) {
        return new EventOutboxCommand(
                event.getAggregateId(),
                event.getAggregateType(),
                event.getEventType()
        );
    }
}
