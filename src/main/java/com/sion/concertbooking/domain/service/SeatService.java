package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.dto.SeatDto;
import com.sion.concertbooking.domain.entity.Seat;
import com.sion.concertbooking.domain.repository.SeatRepository;
import org.springframework.stereotype.Service;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(final SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public SeatDto getSeatById(long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좌석입니다."));
        return SeatDto.fromEntity(seat);
    }
}
