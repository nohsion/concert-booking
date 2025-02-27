package com.sion.concertbooking.intefaces.presentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.application.reservation.ReservationResult;
import com.sion.concertbooking.domain.reservation.Reservation;
import com.sion.concertbooking.domain.seat.Seat;

import java.time.LocalDateTime;

public record ReservationResponse(
        @JsonProperty(value = "reservationId") long reservationId,
        @JsonProperty(value = "concertId") long concertId,
        @JsonProperty(value = "concertTitle") String concertTitle,
        @JsonProperty(value = "concertScheduleId") long concertScheduleId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonProperty(value = "playDateTime") LocalDateTime playDateTime,
        @JsonProperty(value = "seatId") long seatId,
        @JsonProperty(value = "seatNum") int seatNum,
        @JsonProperty(value = "seatGrade") Seat.Grade seatGrade,
        @JsonProperty(value = "seatPrice") int seatPrice,
        @JsonProperty(value = "reservationStatus") Reservation.Status reservationStatus
) {

    public static ReservationResponse fromResult(ReservationResult result) {
        return new ReservationResponse(
                result.reservationId(),
                result.concertId(),
                result.concertTitle(),
                result.concertScheduleId(),
                result.playDateTime(),
                result.seatId(),
                result.seatNum(),
                result.seatGrade(),
                result.seatPrice(),
                result.reservationStatus()
        );
    }
}
