package com.sion.concertbooking.application;

import com.sion.concertbooking.application.result.ReservationResult;
import com.sion.concertbooking.domain.reservation.ReservationCreateCommand;
import com.sion.concertbooking.domain.concert.ConcertInfo;
import com.sion.concertbooking.domain.concertschedule.ConcertScheduleInfo;
import com.sion.concertbooking.domain.reservation.ReservationInfo;
import com.sion.concertbooking.domain.seat.SeatInfo;
import com.sion.concertbooking.domain.concertschedule.ConcertScheduleService;
import com.sion.concertbooking.domain.concert.ConcertService;
import com.sion.concertbooking.domain.reservation.ReservationService;
import com.sion.concertbooking.domain.seat.SeatService;
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

        // 예약할 공연과 공연 일정을 가져온다.
        ConcertInfo concert = concertService.getConcertById(concertId);
        ConcertScheduleInfo concertSchedule = concertScheduleService.getConcertScheduleById(concertScheduleId);

        // 예약할 좌석들을 가져온다.
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

        // 예약을 시도한다. 만약 이미 예약된 좌석이라면 실패한다.
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
