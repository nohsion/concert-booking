package com.sion.concertbooking.domain.reservation;

import com.sion.concertbooking.domain.seat.Seat;

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
        Seat.Grade seatGrade,
        int seatPrice,
        Reservation.Status reservationStatus,
        LocalDateTime expiredAt
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
                reservation.getStatus(),
                reservation.getExpiredAt()
        );
    }
}
