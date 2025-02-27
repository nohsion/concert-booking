package com.sion.concertbooking.domain.concertschedule;

import java.util.List;
import java.util.Optional;

public interface ConcertScheduleRepository {
    Optional<ConcertSchedule> findById(long concertScheduleId);
    ConcertSchedule save(ConcertSchedule concertSchedule);
    List<ConcertSchedule> findByConcertId(long concertId);
}
