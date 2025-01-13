package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.entity.Seat;

import java.util.Optional;

public interface SeatRepository {
    Optional<Seat> findById(long seatId);
    Seat save(Seat seat);
}
