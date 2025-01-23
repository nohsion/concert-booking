package com.sion.concertbooking.domain.reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> saveAll(List<Reservation> reservations);

    List<Reservation> findByConcertScheduleIdAndSeatIds(long concertScheduleId, List<Long> seatIds);

    List<Reservation> findByConcertScheduleIdAndSeatIdsWithLock(long concertScheduleId, List<Long> seatIds);

    Optional<Reservation> findById(long reservationId);
}
