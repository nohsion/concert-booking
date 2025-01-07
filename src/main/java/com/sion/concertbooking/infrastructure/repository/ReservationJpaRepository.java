package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByConcertScheduleIdAndSeatId(long concertScheduleId, long seatId);
}
