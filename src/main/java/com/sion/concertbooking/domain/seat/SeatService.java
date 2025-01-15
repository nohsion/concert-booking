package com.sion.concertbooking.domain.seat;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(final SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<SeatInfo> getSeatsById(List<Long> seatIds) {
        List<Seat> seats = seatIds.stream()
                .map(seatId -> seatRepository.findById(seatId)
                        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 좌석입니다. seatId=" + seatId)))
                .toList();
        return seats.stream()
                .map(SeatInfo::fromEntity)
                .toList();
    }
}
