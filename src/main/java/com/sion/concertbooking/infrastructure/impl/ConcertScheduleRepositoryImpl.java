package com.sion.concertbooking.infrastructure.impl;

import com.sion.concertbooking.domain.concertschedule.ConcertSchedule;
import com.sion.concertbooking.domain.concertschedule.ConcertScheduleRepository;
import com.sion.concertbooking.infrastructure.jpa.ConcertScheduleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ConcertScheduleRepositoryImpl implements ConcertScheduleRepository {

    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;

    public ConcertScheduleRepositoryImpl(final ConcertScheduleJpaRepository concertScheduleJpaRepository) {
        this.concertScheduleJpaRepository = concertScheduleJpaRepository;
    }

    @Override
    public Optional<ConcertSchedule> findById(final long concertScheduleId) {
        return concertScheduleJpaRepository.findById(concertScheduleId);
    }

    @Override
    public ConcertSchedule save(final ConcertSchedule concertSchedule) {
        return concertScheduleJpaRepository.save(concertSchedule);
    }

    @Override
    public ConcertSchedule findByConcertId(final long concertId) {
        return concertScheduleJpaRepository.findByConcertId(concertId);
    }
}
