package com.sion.concertbooking.application;

import com.sion.concertbooking.application.result.ReservationResult;
import com.sion.concertbooking.domain.command.ReservationCreateCommand;
import com.sion.concertbooking.domain.info.ConcertInfo;
import com.sion.concertbooking.domain.info.ConcertScheduleInfo;
import com.sion.concertbooking.domain.info.ReservationInfo;
import com.sion.concertbooking.domain.info.SeatInfo;
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
    public List<ReservationResult> reserve(long userId, ConcertReservationCreateRequest concertReservationCreateRequest) {
        LocalDateTime now = LocalDateTime.now();
        long concertId = concertReservationCreateRequest.concertId();
        long concertScheduleId = concertReservationCreateRequest.concertScheduleId();
        List<Long> seatIds = concertReservationCreateRequest.seatIds();

        // 0. 콘서트 정보를 가져온다.
        ConcertInfo concert = concertService.getConcertById(concertId);
        ConcertScheduleInfo concertSchedule = concertScheduleService.getConcertScheduleById(concertScheduleId);

        // 1. 예약된 좌석이 없는지 확인한다.
        boolean allSeatsAvailable = seatIds.stream()
                .noneMatch(seatId -> reservationService.isReservedSeat(concertScheduleId, seatId, now));
        if (!allSeatsAvailable) {
            throw new IllegalArgumentException("이미 예약중인 좌석입니다.");
        }

        // 2. 좌석 정보를 가져온다.
        List<ReservationCreateCommand.SeatCreateCommand> seatCreateCommands = seatIds.stream()
                .map(seatId -> {
                    SeatInfo seat = seatService.getSeatById(seatId);
                    return new ReservationCreateCommand.SeatCreateCommand(
                            seatId,
                            seat.seatNum(),
                            seat.seatGrade(),
                            seat.seatPrice()
                    );
                })
                .toList();

        // 3. 예약된 좌석이 없다면, 해당 좌석들을 예약한다.
        ReservationCreateCommand reservationCreateCommand = ReservationCreateCommand.builder()
                .userId(userId)
                .concertId(concertId)
                .concertTitle(concert.concertTitle())
                .concertScheduleId(concertScheduleId)
                .playDateTime(concertSchedule.playDateTime())
                .now(now)
                .seats(seatCreateCommands)
                .build();

        List<ReservationInfo> reservations = reservationService.createReservations(reservationCreateCommand);
        return reservations.stream()
                .map(ReservationResult::fromInfo)
                .toList();
    }
}
