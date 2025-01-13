package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.entity.ConcertSchedule;
import com.sion.concertbooking.domain.repository.ConcertScheduleRepository;
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
}
