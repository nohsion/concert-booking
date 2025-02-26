package com.sion.concertbooking.application.concert;

import com.sion.concertbooking.domain.concertschedule.ConcertScheduleInfo;
import com.sion.concertbooking.domain.concertschedule.ConcertScheduleService;
import com.sion.concertbooking.domain.reservation.ReservationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcertFacade {

    private static final int SEAT_CAPACITY = 50;

    private final ConcertScheduleService concertScheduleService;
    private final ReservationService reservationService;

    public ConcertFacade(
            ConcertScheduleService concertScheduleService,
            ReservationService reservationService
    ) {
        this.concertScheduleService = concertScheduleService;
        this.reservationService = reservationService;
    }

    public List<ConcertScheduleResult> getConcertSchedules(long concertId) {
        List<ConcertScheduleInfo> concertScheduleInfos = concertScheduleService.getConcertSchedulesByConcertId(concertId);
        return concertScheduleInfos.stream()
                .map(scheduleInfo -> {
                    long concertScheduleId = scheduleInfo.concertScheduleId();
                    long reservedCount = reservationService.getReservedSeatCountByConcertScheduleId(concertScheduleId);
                    int remainingSeatCount = Math.max(0, SEAT_CAPACITY - (int) reservedCount);
                    return ConcertScheduleResult.fromInfo(scheduleInfo, remainingSeatCount);
                }).toList();
    }
}
