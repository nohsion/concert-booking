package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.model.entity.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> saveAll(List<Reservation> reservations);

    List<Reservation> findByConcertScheduleIdAndSeatId(long concertScheduleId, long seatId);
}
