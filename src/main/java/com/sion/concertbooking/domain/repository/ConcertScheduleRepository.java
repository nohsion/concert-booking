package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.entity.ConcertSchedule;

import java.util.Optional;

public interface ConcertScheduleRepository {
    Optional<ConcertSchedule> findById(final long concertScheduleId);
}
