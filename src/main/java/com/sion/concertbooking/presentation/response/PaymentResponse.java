package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.application.result.PaymentResult;
import com.sion.concertbooking.domain.info.ReservationInfo;

import java.util.List;

public record PaymentResponse(
        @JsonProperty("userId") long userId,
        @JsonProperty("pointAmount") int pointAmount,
        @JsonProperty("pointBalance") int pointBalance,
        @JsonProperty("reservations") List<ReservationInfo> reservations
) {
    public static PaymentResponse fromResult(PaymentResult result) {
        return new PaymentResponse(
                result.userId(),
                result.pointAmount(),
                result.pointBalance(),
                result.reservations()
        );
    }
}
