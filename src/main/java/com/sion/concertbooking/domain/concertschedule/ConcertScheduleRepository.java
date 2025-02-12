package com.sion.concertbooking.domain.concertschedule;

import java.util.Optional;

public interface ConcertScheduleRepository {
    Optional<ConcertSchedule> findById(long concertScheduleId);
    ConcertSchedule save(ConcertSchedule concertSchedule);
    ConcertSchedule findByConcertId(long concertId);
}
