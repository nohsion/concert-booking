package com.sion.concertbooking.domain.seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    Optional<Seat> findById(long seatId);
    Seat save(Seat seat);
    List<Seat> findAllById(List<Long> seatIds);
}
