package com.sion.concertbooking.presentation.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ReservationCreateRequest(
        @JsonProperty(value = "concertId") long concertId,
        @JsonProperty(value = "concertScheduleId") long concertScheduleId,
        @JsonProperty(value = "seatIds") List<Long> seatIds
) {
}
