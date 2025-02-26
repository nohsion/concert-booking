package com.sion.concertbooking.domain.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.event.Event;
import com.sion.concertbooking.domain.reservation.ReservationInfo;

import java.util.List;

public final class PaymentRequestEvent extends Event {

    public static final String AGGEREGATE_TYPE = "payment";
    public static final String EVENT_TYPE = "paymentRequest";

    @JsonProperty("paymentId")
    private final long paymentId;
    @JsonProperty("userId")
    private final long userId;
    @JsonProperty("totalPrice")
    private final long totalPrice;
    @JsonProperty("tokenId")
    private final String tokenId;
    @JsonProperty("concertId")
    private final long concertId;
    @JsonProperty("reservations")
    private final List<ReservationInfo> reservations;

    public PaymentRequestEvent(
            long paymentId,
            long userId,
            long totalPrice,
            String tokenId,
            long concertId,
            List<ReservationInfo> reservations
    ) {
        super(AGGEREGATE_TYPE, EVENT_TYPE, paymentId);
        this.paymentId = paymentId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.tokenId = tokenId;
        this.concertId = concertId;
        this.reservations = reservations;
    }

    public static PaymentRequestEvent of(
            PaymentInfo paymentInfo,
            String tokenId,
            long concertId,
            List<ReservationInfo> reservations
    ) {
        return new PaymentRequestEvent(
                paymentInfo.id(),
                paymentInfo.userId(),
                paymentInfo.amount(),
                tokenId,
                concertId,
                reservations
        );
    }

}
