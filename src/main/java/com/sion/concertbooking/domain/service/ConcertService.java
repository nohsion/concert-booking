package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.model.entity.Concert;
import com.sion.concertbooking.domain.model.info.ConcertInfo;
import com.sion.concertbooking.domain.repository.ConcertRepository;
import org.springframework.stereotype.Service;

@Service
public class ConcertService {

    private final ConcertRepository concertRepository;

    public ConcertService(final ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public ConcertInfo getConcertById(final long concertId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘서트입니다."));
        return ConcertInfo.fromEntity(concert);
    }
}
