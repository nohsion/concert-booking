package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.model.entity.Reservation;
import com.sion.concertbooking.domain.repository.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationCoreRepository implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;

    public ReservationCoreRepository(
            final ReservationJpaRepository reservationJpaRepository
    ) {
        this.reservationJpaRepository = reservationJpaRepository;
    }


    @Override
    public List<Reservation> saveAll(List<Reservation> reservations) {
        return reservationJpaRepository.saveAll(reservations);
    }

    @Override
    public List<Reservation> findByConcertScheduleIdAndSeatId(final long concertScheduleId, final long seatId) {
        return reservationJpaRepository.findByConcertScheduleIdAndSeatId(concertScheduleId, seatId);
    }
}
