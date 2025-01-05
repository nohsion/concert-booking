package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.dto.ReservationDto;
import com.sion.concertbooking.domain.dto.UserPointDto;

import java.util.List;

public record PaymentResponse(
        @JsonProperty(value = "userPoint") UserPointDto userPoint,
        @JsonProperty(value = "reservations") List<ReservationDto> reservations
) {
}
