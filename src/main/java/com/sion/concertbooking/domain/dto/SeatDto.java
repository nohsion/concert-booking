package com.sion.concertbooking.domain.dto;

import com.sion.concertbooking.domain.entity.Seat;
import com.sion.concertbooking.domain.enums.SeatGrade;

public record SeatDto(
        long seatId,
        long theaterId,
        int seatNum,
        SeatGrade seatGrade,
        int seatPrice
) {
    public static SeatDto fromEntity(Seat seat) {
        return new SeatDto(
                seat.getId(),
                seat.getTheatreId(),
                seat.getSeatNum(),
                seat.getSeatGrade(),
                seat.getSeatPrice()
        );
    }
}
