package com.sion.concertbooking.domain.seat;

public record SeatInfo(
        long seatId,
        long theaterId,
        int seatNum,
        Seat.Grade seatGrade,
        int seatPrice
) {
    public static SeatInfo fromEntity(Seat seat) {
        return new SeatInfo(
                seat.getId(),
                seat.getTheatreId(),
                seat.getSeatNum(),
                seat.getSeatGrade(),
                seat.getSeatPrice()
        );
    }
}
