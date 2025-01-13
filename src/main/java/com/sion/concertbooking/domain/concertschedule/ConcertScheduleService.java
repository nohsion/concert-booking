package com.sion.concertbooking.domain.concertschedule;

import org.springframework.stereotype.Service;

@Service
public class ConcertScheduleService {

    private final ConcertScheduleRepository concertScheduleRepository;

    public ConcertScheduleService(final ConcertScheduleRepository concertScheduleRepository) {
        this.concertScheduleRepository = concertScheduleRepository;
    }

    public ConcertScheduleInfo getConcertScheduleById(final long concertScheduleId) {
        ConcertSchedule concertSchedule = concertScheduleRepository.findById(concertScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연 일정입니다."));
        return ConcertScheduleInfo.fromEntity(concertSchedule);
    }
}
