package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.model.entity.ConcertSchedule;
import com.sion.concertbooking.domain.model.info.ConcertScheduleInfo;
import com.sion.concertbooking.domain.repository.ConcertScheduleRepository;
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
