package com.sion.concertbooking.domain.seat;

import java.util.Optional;

public interface SeatRepository {
    Optional<Seat> findById(long seatId);
    Seat save(Seat seat);
}
