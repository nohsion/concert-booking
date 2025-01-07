package com.sion.concertbooking.domain.model.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.model.entity.Reservation;
import com.sion.concertbooking.domain.enums.ReservationStatus;
import com.sion.concertbooking.domain.enums.SeatGrade;

import java.time.LocalDateTime;

public record ReservationInfo(
        @JsonProperty(value = "reservationId") long reservationId,
        @JsonProperty(value = "userId") long userId,
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

    public static ReservationInfo ofEntity(Reservation reservation) {
        return new ReservationInfo(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getConcertId(),
                reservation.getConcertTitle(),
                reservation.getConcertScheduleId(),
                reservation.getPlayDateTime(),
                reservation.getSeatId(),
                reservation.getSeatNum(),
                reservation.getSeatGrade(),
                reservation.getSeatPrice(),
                reservation.getStatus()
        );
    }
}
