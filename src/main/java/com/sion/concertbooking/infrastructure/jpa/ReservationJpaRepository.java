package com.sion.concertbooking.infrastructure.jpa;

import jakarta.persistence.LockModeType;
import com.sion.concertbooking.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByConcertScheduleIdAndSeatIdIn(long concertScheduleId, List<Long> seatIds);

    List<Reservation> findByUserId(long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reservation r WHERE r.concertScheduleId = :concertScheduleId AND r.seatId IN :seatIds")
    List<Reservation> findByConcertScheduleIdAndSeatIdsWithLock(
            @Param("concertScheduleId") long concertScheduleId,
            @Param("seatIds") List<Long> seatIds
    );

    List<Reservation> findByConcertScheduleId(long concertScheduleId);
}
