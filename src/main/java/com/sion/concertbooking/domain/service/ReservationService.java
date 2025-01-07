package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.command.ReservationCreateCommand;
import com.sion.concertbooking.domain.entity.Reservation;
import com.sion.concertbooking.domain.info.ReservationInfo;
import com.sion.concertbooking.domain.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(
            final ReservationRepository reservationRepository
    ) {
        this.reservationRepository = reservationRepository;
    }

    public boolean isReservedSeat(long concertScheduleId, long seatId, LocalDateTime now) {
        return reservationRepository.findByConcertScheduleIdAndSeatId(concertScheduleId, seatId)
                .stream()
                // 예약완료 혹은 결제대기 중인 예약이 하나라도 있는지 확인
                .anyMatch(reservation -> reservation.isReserved() || (reservation.isSuspend(now)));
    }

    public boolean isExpiredReservation(long reservationId, LocalDateTime now) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
        return reservation.isExpired(now);
    }

    @Transactional
    public void completeReservation(long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
        reservation.markSuccess();
    }

    public ReservationInfo getReservationById(long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
        return ReservationInfo.ofEntity(reservation);
    }

    @Transactional
    public List<ReservationInfo> createReservations(ReservationCreateCommand createDto) {
        List<ReservationCreateCommand.SeatCreateCommand> seats = createDto.getSeats();
        List<Reservation> reservations = seats.stream()
                .map(seat -> Reservation.of(
                        createDto.getUserId(),
                        createDto.getConcertId(),
                        createDto.getConcertTitle(),
                        createDto.getConcertScheduleId(),
                        createDto.getPlayDateTime(),
                        createDto.getNow(),
                        seat.getSeatId(),
                        seat.getSeatNum(),
                        seat.getSeatGrade(),
                        seat.getSeatPrice()
                ))
                .toList();
        List<Reservation> savedReservations = reservationRepository.saveAll(reservations);
        return savedReservations.stream()
                .map(ReservationInfo::ofEntity)
                .toList();
    }
}
