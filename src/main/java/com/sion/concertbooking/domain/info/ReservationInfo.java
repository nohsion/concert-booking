package com.sion.concertbooking.domain.info;

import com.sion.concertbooking.domain.entity.Reservation;
import com.sion.concertbooking.domain.enums.ReservationStatus;
import com.sion.concertbooking.domain.enums.SeatGrade;

import java.time.LocalDateTime;

public record ReservationInfo(
        long reservationId,
        long userId,
        long concertId,
        String concertTitle,
        long concertScheduleId,
        LocalDateTime playDateTime,
        long seatId,
        int seatNum,
        SeatGrade seatGrade,
        int seatPrice,
        ReservationStatus reservationStatus
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
