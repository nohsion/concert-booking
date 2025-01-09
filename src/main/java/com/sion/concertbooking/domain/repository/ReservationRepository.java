package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> saveAll(List<Reservation> reservations);

    List<Reservation> findByConcertScheduleIdAndSeatIdsWithLock(long concertScheduleId, List<Long> seatIds);

    Optional<Reservation> findById(long reservationId);
}
