package com.sion.concertbooking.domain.reservation;

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

    @Transactional
    public boolean isReservedSeats(long concertScheduleId, List<Long> seatIds, LocalDateTime now) {
        return reservationRepository.findByConcertScheduleIdAndSeatIdsWithLock(concertScheduleId, seatIds)
                .stream()
                // 예약완료 혹은 결제대기 중인 예약이 하나라도 있는지 확인
                .anyMatch(reservation -> reservation.isReserved() || (reservation.isSuspend(now)));
    }

    public boolean checkExpiredReservations(List<Long> reservationIds, LocalDateTime now) {
        return reservationIds.stream()
                .anyMatch(reservationId -> {
                    Reservation reservation = reservationRepository.findById(reservationId)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
                    return reservation.isExpired(now);
                });

    }

    @Transactional
    public void completeReservations(List<Long> reservationIds) {
        reservationIds.stream()
                .map(reservationId -> reservationRepository.findById(reservationId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다.")))
                .forEach(Reservation::markSuccess);
    }

    public ReservationInfo getReservationById(long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
        return ReservationInfo.ofEntity(reservation);
    }

    @Transactional
    public List<ReservationInfo> createReservations(ReservationCreateCommand command) {
        LocalDateTime now = command.getNow();
        long concertScheduleId = command.getConcertScheduleId();
        List<ReservationCreateCommand.SeatCreateCommand> seats = command.getSeats();
        List<Long> seatIds = seats.stream().map(ReservationCreateCommand.SeatCreateCommand::getSeatId).toList();

        // 1. 예약된 좌석이 없는지 확인한다.
        boolean isReserved = reservationRepository.findByConcertScheduleIdAndSeatIdsWithLock(concertScheduleId, seatIds)
                .stream()
                // 예약완료 혹은 결제대기 중인 예약이 하나라도 있는지 확인
                .anyMatch(reservation -> reservation.isReserved() || (reservation.isSuspend(now)));
        if (isReserved) {
            throw new IllegalArgumentException("이미 예약중인 좌석입니다.");
        }

        // 2. 모든 좌석에 대해 예약을 생성한다.
        List<Reservation> reservations = seats.stream()
                .map(seat -> Reservation.createReservation(
                        command.getUserId(),
                        command.getConcertId(),
                        command.getConcertTitle(),
                        command.getConcertScheduleId(),
                        command.getPlayDateTime(),
                        command.getNow(),
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

    public int getTotalPrice(List<ReservationInfo> reservations) {
        return reservations.stream()
                .mapToInt(ReservationInfo::seatPrice)
                .sum();
    }
}
