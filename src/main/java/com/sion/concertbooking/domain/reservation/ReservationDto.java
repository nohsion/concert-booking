package com.sion.concertbooking.domain.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.seat.SeatGrade;

import java.time.LocalDateTime;

public record ReservationDto(
        @JsonProperty(value = "reservationId") long reservationId,
        @JsonProperty(value = "concertId") long concertId,
        @JsonProperty(value = "concertTitle") String concertTitle,
        @JsonProperty(value = "concertScheduleId") long concertScheduleId,
        @JsonProperty(value = "playDateTime") LocalDateTime playDateTime,
        @JsonProperty(value = "seatId") long seatId,
        @JsonProperty(value = "seatNum") int seatNum,
        @JsonProperty(value = "seatGrade") SeatGrade seatGrade,
        @JsonProperty(value = "seatPrice") int seatPrice,
        @JsonProperty(value = "reservationStatus") ReservationStatus reservationStatus
) {
}
