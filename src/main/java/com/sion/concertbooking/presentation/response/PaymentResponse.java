package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.dto.ReservationDto;
import com.sion.concertbooking.domain.dto.PointDto;

import java.util.List;

public record PaymentResponse(
        @JsonProperty(value = "point") PointDto point,
        @JsonProperty(value = "reservations") List<ReservationDto> reservations
) {
}
