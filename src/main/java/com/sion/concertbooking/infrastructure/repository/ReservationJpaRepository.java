package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.entity.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reservation r WHERE r.concertScheduleId = :concertScheduleId AND r.seatId = :seatId")
    List<Reservation> findByConcertScheduleIdAndSeatIdWithLock(
            @Param("concertScheduleId") long concertScheduleId,
            @Param("seatId") long seatId
    );
}
