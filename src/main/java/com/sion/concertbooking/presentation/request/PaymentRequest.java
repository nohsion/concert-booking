package com.sion.concertbooking.presentation.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaymentRequest(
        @JsonProperty("reservationIds") List<Long> reservationIds
) {
}
