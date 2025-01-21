package com.sion.concertbooking.application.reservation;

import com.sion.concertbooking.domain.reservation.Reservation;
import com.sion.concertbooking.domain.seat.Seat;
import com.sion.concertbooking.domain.reservation.ReservationInfo;

import java.time.LocalDateTime;

public record ReservationResult(
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
        Reservation.Status reservationStatus
) {
    public static ReservationResult fromInfo(ReservationInfo info) {
        return new ReservationResult(
                info.reservationId(),
                info.userId(),
                info.concertId(),
                info.concertTitle(),
                info.concertScheduleId(),
                info.playDateTime(),
                info.seatId(),
                info.seatNum(),
                info.seatGrade(),
                info.seatPrice(),
                info.reservationStatus()
        );
    }
}
