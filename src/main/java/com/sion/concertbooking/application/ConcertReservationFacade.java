package com.sion.concertbooking.application;

import com.sion.concertbooking.domain.dto.*;
import com.sion.concertbooking.domain.service.ConcertScheduleService;
import com.sion.concertbooking.domain.service.ConcertService;
import com.sion.concertbooking.domain.service.ReservationService;
import com.sion.concertbooking.domain.service.SeatService;
import com.sion.concertbooking.presentation.request.ConcertReservationCreateRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ConcertReservationFacade {

    private final ReservationService reservationService;
    private final ConcertService concertService;
    private final ConcertScheduleService concertScheduleService;
    private final SeatService seatService;

    public ConcertReservationFacade(
            final ReservationService reservationService,
            final ConcertService concertService,
            final ConcertScheduleService concertScheduleService,
            final SeatService seatService
    ) {
        this.reservationService = reservationService;
        this.concertService = concertService;
        this.concertScheduleService = concertScheduleService;
        this.seatService = seatService;
    }

    @Transactional
    public List<ReservationDto> reserve(long userId, ConcertReservationCreateRequest concertReservationCreateRequest) {
        LocalDateTime now = LocalDateTime.now();
        long concertId = concertReservationCreateRequest.concertId();
        long concertScheduleId = concertReservationCreateRequest.concertScheduleId();
        List<Long> seatIds = concertReservationCreateRequest.seatIds();

        // 0. 콘서트 정보를 가져온다.
        ConcertDto concert = concertService.getConcertById(concertId);
        ConcertScheduleDto concertSchedule = concertScheduleService.getConcertScheduleById(concertScheduleId);

        // 1. 예약된 좌석이 없는지 확인한다.
        boolean allSeatsAvailable = seatIds.stream()
                .noneMatch(seatId -> reservationService.isReservedSeat(concertScheduleId, seatId, now));
        if (!allSeatsAvailable) {
            throw new IllegalArgumentException("이미 예약된 좌석입니다.");
        }

        // 2. 좌석 정보를 가져온다.
        List<ReservationCreateDto.SeatCreateDto> seatCreateDtos = seatIds.stream()
                .map(seatId -> {
                    SeatDto seat = seatService.getSeatById(seatId);
                    return new ReservationCreateDto.SeatCreateDto(
                            seatId,
                            seat.seatNum(),
                            seat.seatGrade(),
                            seat.seatPrice()
                    );
                })
                .toList();

        // 3. 예약된 좌석이 없다면, 해당 좌석들을 예약한다.
        ReservationCreateDto reservationCreateDto = ReservationCreateDto.builder()
                .userId(userId)
                .concertId(concertId)
                .concertTitle(concert.concertTitle())
                .concertScheduleId(concertScheduleId)
                .playDateTime(concertSchedule.playDateTime())
                .now(now)
                .seats(seatCreateDtos)
                .build();

        return reservationService.createReservations(reservationCreateDto);
    }
}
