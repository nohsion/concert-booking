package com.sion.concertbooking.infrastructure.jpa;

import com.sion.concertbooking.domain.seat.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
}
