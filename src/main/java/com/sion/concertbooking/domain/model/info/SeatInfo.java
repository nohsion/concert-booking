package com.sion.concertbooking.domain.model.info;

import com.sion.concertbooking.domain.model.entity.Seat;
import com.sion.concertbooking.domain.enums.SeatGrade;

public record SeatInfo(
        long seatId,
        long theaterId,
        int seatNum,
        SeatGrade seatGrade,
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
