package com.sion.concertbooking.infrastructure.impl;

import com.sion.concertbooking.domain.reservation.Reservation;
import com.sion.concertbooking.domain.reservation.ReservationRepository;
import com.sion.concertbooking.infrastructure.jpa.ReservationJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepositoryIml implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;

    public ReservationRepositoryIml(
            final ReservationJpaRepository reservationJpaRepository
    ) {
        this.reservationJpaRepository = reservationJpaRepository;
    }


    @Override
    public Reservation save(final Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    @Override
    public List<Reservation> saveAll(List<Reservation> reservations) {
        return reservationJpaRepository.saveAll(reservations);
    }

    @Override
    public List<Reservation> findByConcertScheduleIdAndSeatIds(final long concertScheduleId, final List<Long> seatIds) {
        return reservationJpaRepository.findByConcertScheduleIdAndSeatIdIn(concertScheduleId, seatIds);
    }

    @Override
    public List<Reservation> findByConcertScheduleIdAndSeatIdsWithLock(final long concertScheduleId, final List<Long> seatIds) {
        return reservationJpaRepository.findByConcertScheduleIdAndSeatIdsWithLock(concertScheduleId, seatIds);
    }

    @Override
    public Optional<Reservation> findById(final long reservationId) {
        return reservationJpaRepository.findById(reservationId);
    }
}
