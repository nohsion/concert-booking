package com.sion.concertbooking.domain.seat;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(final SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public SeatInfo getSeatById(long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좌석입니다."));
        return SeatInfo.fromEntity(seat);
    }

    public List<SeatInfo> getSeatsById(List<Long> seatIds) {
        List<Seat> seats = seatRepository.findAllById(seatIds);
        return seats.stream()
                .map(SeatInfo::fromEntity)
                .toList();
    }
}
