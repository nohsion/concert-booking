package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.seat.Seat;
import com.sion.concertbooking.domain.seat.SeatRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;

    public SeatRepositoryImpl(SeatJpaRepository seatJpaRepository) {
        this.seatJpaRepository = seatJpaRepository;
    }

    @Override
    public Optional<Seat> findById(final long seatId) {
        return seatJpaRepository.findById(seatId);
    }

    @Override
    public Seat save(final Seat seat) {
        return seatJpaRepository.save(seat);
    }
}
