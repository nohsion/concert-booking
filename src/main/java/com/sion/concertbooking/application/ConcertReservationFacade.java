package com.sion.concertbooking.application;

import com.sion.concertbooking.domain.dto.*;
import com.sion.concertbooking.domain.entity.Concert;
import com.sion.concertbooking.domain.entity.ConcertSchedule;
import com.sion.concertbooking.domain.entity.Reservation;
import com.sion.concertbooking.domain.entity.Seat;
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
        Concert concert = concertService.getConcertById(concertId);
        ConcertSchedule concertSchedule = concertScheduleService.getConcertScheduleById(concertScheduleId);

        // 1. 예약된 좌석이 없는지 확인한다.
        boolean allSeatsAvailable = seatIds.stream()
                .noneMatch(seatId -> reservationService.isReservedSeat(concertScheduleId, seatId, now));
        if (!allSeatsAvailable) {
            throw new IllegalArgumentException("이미 예약중인 좌석입니다.");
        }

        // 2. 좌석 정보를 가져온다.
        List<ReservationCreateDto.SeatCreateDto> seatCreateDtos = seatIds.stream()
                .map(seatId -> {
                    Seat seat = seatService.getSeatById(seatId);
                    return new ReservationCreateDto.SeatCreateDto(
                            seatId,
                            seat.getSeatNum(),
                            seat.getSeatGrade(),
                            seat.getSeatPrice()
                    );
                })
                .toList();

        // 3. 예약된 좌석이 없다면, 해당 좌석들을 예약한다.
        ReservationCreateDto reservationCreateDto = ReservationCreateDto.builder()
                .userId(userId)
                .concertId(concertId)
                .concertTitle(concert.getTitle())
                .concertScheduleId(concertScheduleId)
                .playDateTime(concertSchedule.getPlayDateTime())
                .now(now)
                .seats(seatCreateDtos)
                .build();

        List<Reservation> reservations = reservationService.createReservations(reservationCreateDto);
        return reservations.stream()
                .map(ReservationDto::ofEntity)
                .toList();
    }
}
