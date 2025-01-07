package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.info.ReservationInfo;
import com.sion.concertbooking.domain.info.PointInfo;

import java.util.List;

public record PaymentResponse(
        @JsonProperty(value = "amount") PointInfo point,
        @JsonProperty(value = "reservations") List<ReservationInfo> reservations
) {
}
