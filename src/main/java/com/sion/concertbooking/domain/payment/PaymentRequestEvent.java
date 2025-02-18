package com.sion.concertbooking.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sion.concertbooking.domain.event.EventOutbox;
import com.sion.concertbooking.domain.event.EventOutboxUtils;
import com.sion.concertbooking.domain.reservation.ReservationInfo;

import java.util.List;

public record PaymentRequestEvent(
        String tokenId,
        long concertId,
        long userId,
        int totalPrice,
        List<ReservationInfo> reservations
) {

    public String toPayload() {
        try {
            return EventOutboxUtils.OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize PaymentEvent", e);
        }
    }

    public EventOutbox toEventOutbox() {
        return EventOutbox.of(
                "payment",
                0, // payment는 테이블이 없으므로 0으로 설정
                "paymentRequest",
                toPayload()
        );
    }
}
