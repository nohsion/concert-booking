package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.reservation.ReservationDto;
import com.sion.concertbooking.domain.userpoint.UserPointDto;

import java.util.List;

public record PaymentResponse(
        @JsonProperty(value = "userPoint") UserPointDto userPoint,
        @JsonProperty(value = "reservations") List<ReservationDto> reservations
) {
}
