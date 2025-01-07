package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.model.info.ReservationInfo;
import com.sion.concertbooking.domain.model.info.PointInfo;

import java.util.List;

public record PaymentResponse(
        @JsonProperty(value = "point") PointInfo point,
        @JsonProperty(value = "reservations") List<ReservationInfo> reservations
) {
}
