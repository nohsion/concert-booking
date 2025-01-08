package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> saveAll(List<Reservation> reservations);

    List<Reservation> findByConcertScheduleIdAndSeatId(long concertScheduleId, long seatId);

    Optional<Reservation> findById(long reservationId);
}
