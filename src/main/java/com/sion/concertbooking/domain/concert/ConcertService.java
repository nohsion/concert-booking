package com.sion.concertbooking.domain.concert;

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
