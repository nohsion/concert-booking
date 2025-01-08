package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.command.ReservationCreateCommand;
import com.sion.concertbooking.domain.entity.Reservation;
import com.sion.concertbooking.domain.enums.ReservationStatus;
import com.sion.concertbooking.domain.info.ReservationInfo;
import com.sion.concertbooking.domain.repository.ReservationRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReservationServiceTest {

    private ReservationService sut;

    private ReservationRepository reservationRepository = mock(ReservationRepository.class);

    @BeforeEach
    void setUp() {
        sut = new ReservationService(reservationRepository);
    }

    @DisplayName("스케쥴ID, 좌석ID에 대해 예약된 좌석이 하나도 없다면 false를 반환한다.")
    @Test
    void isReservedSeatFalseWhenReservationIsEmpty() {
        // given
        long concertScheduleId = 1L;
        long seatId = 1L;
        LocalDateTime now = LocalDateTime.of(2025, 1, 6, 23, 0, 0);

        when(reservationRepository.findByConcertScheduleIdAndSeatIdWithLock(concertScheduleId, seatId))
                .thenReturn(List.of());

        // when
        boolean result = sut.isReservedSeat(concertScheduleId, seatId, now);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("스케쥴ID, 좌석ID에 대해 예약된 좌석이 모두 취소좌석이라면 false를 반환한다.")
    @Test
    void isReservedSeatFalseWhenAllReservationIsCanceled() {
        // given
        long concertScheduleId = 1L;
        long seatId = 1L;
        LocalDateTime now = LocalDateTime.of(2025, 1, 6, 23, 0, 0);
        List<Reservation> reservations = Instancio.ofList(Reservation.class).size(3)
                .set(field(Reservation::getStatus), ReservationStatus.CANCEL)
                .create();

        when(reservationRepository.findByConcertScheduleIdAndSeatIdWithLock(concertScheduleId, seatId))
                .thenReturn(reservations);

        // when
        boolean result = sut.isReservedSeat(concertScheduleId, seatId, now);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("스케쥴ID, 좌석ID에 대해 예약된 좌석이 모두 완료된 좌석이라면 true를 반환한다.")
    @Test
    void isReservedSeatTrueWhenAllReservationIsReserved() {
        // given
        long concertScheduleId = 1L;
        long seatId = 1L;
        LocalDateTime now = LocalDateTime.of(2025, 1, 6, 23, 0, 0);
        List<Reservation> reservations = Instancio.ofList(Reservation.class).size(3)
                .set(field(Reservation::getStatus), ReservationStatus.SUCCESS)
                .create();

        when(reservationRepository.findByConcertScheduleIdAndSeatIdWithLock(concertScheduleId, seatId))
                .thenReturn(reservations);

        // when
        boolean result = sut.isReservedSeat(concertScheduleId, seatId, now);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("스케쥴ID, 좌석ID에 대해 예약된 좌석이 모두 결제대기이면서 만료되지 않았다면 true를 반환한다.")
    @Test
    void isReservedSeatTrueWhenAllReservationIsSuspend() {
        // given
        long concertScheduleId = 1L;
        long seatId = 1L;
        LocalDateTime now = LocalDateTime.of(2025, 1, 6, 23, 0, 0);
        List<Reservation> reservations = Instancio.ofList(Reservation.class).size(3)
                .set(field(Reservation::getStatus), ReservationStatus.SUSPEND)
                .set(field(Reservation::getExpiredAt), now.plusMinutes(3)) // 아직 만료되지 않음
                .create();

        when(reservationRepository.findByConcertScheduleIdAndSeatIdWithLock(concertScheduleId, seatId))
                .thenReturn(reservations);

        // when
        boolean result = sut.isReservedSeat(concertScheduleId, seatId, now);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("스케쥴ID, 좌석ID에 대해 예약된 좌석이 모두 결제대기이지만 만료되었다면 false를 반환한다.")
    @Test
    void isReservedSeatFalseWhenAllReservationIsSuspendButExpired() {
        // given
        long concertScheduleId = 1L;
        long seatId = 1L;
        LocalDateTime now = LocalDateTime.of(2025, 1, 6, 23, 0, 0);
        List<Reservation> reservations = Instancio.ofList(Reservation.class).size(3)
                .set(field(Reservation::getStatus), ReservationStatus.SUSPEND)
                .set(field(Reservation::getExpiredAt), now.minusMinutes(3)) // 만료됨
                .create();

        when(reservationRepository.findByConcertScheduleIdAndSeatIdWithLock(concertScheduleId, seatId))
                .thenReturn(reservations);

        // when
        boolean result = sut.isReservedSeat(concertScheduleId, seatId, now);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("스케쥴ID, 좌석ID에 대해 예약된 좌석이 완료된 좌석과 취소된 좌석이 섞여 있다면 true를 반환한다.")
    @Test
    void isReservedSeatTrueWhenReservationIsMixed() {
        // given
        long concertScheduleId = 1L;
        long seatId = 1L;
        LocalDateTime now = LocalDateTime.of(2025, 1, 6, 23, 0, 0);
        List<Reservation> successReservations = Instancio.ofList(Reservation.class).size(3)
                .set(field(Reservation::getStatus), ReservationStatus.SUCCESS)
                .create();
        List<Reservation> cancelReservations = Instancio.ofList(Reservation.class).size(2)
                .set(field(Reservation::getStatus), ReservationStatus.CANCEL)
                .create();
        List<Reservation> mixedReservations = new ArrayList<>();
        mixedReservations.addAll(successReservations);
        mixedReservations.addAll(cancelReservations);

        when(reservationRepository.findByConcertScheduleIdAndSeatIdWithLock(concertScheduleId, seatId))
                .thenReturn(mixedReservations);

        // when
        boolean result = sut.isReservedSeat(concertScheduleId, seatId, now);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("요청 좌석들에 대해 예약에 성공하면 객체 도메인 리스트로 반환한다.")
    @Test
    void createReservationsSuccess() {
        // given
        long userId = 1L;
        long concertId = 1L;
        String concertTitle = "지킬앤하이드 28주년";
        List<Reservation> reservations = Instancio.ofList(Reservation.class).size(3)
                .create();
        List<ReservationCreateCommand.SeatCreateCommand> seats = Instancio.ofList(ReservationCreateCommand.SeatCreateCommand.class)
                .size(3)
                .create();
        ReservationCreateCommand reservationCreateCommand = ReservationCreateCommand.builder()
                .userId(userId)
                .concertId(concertId)
                .concertTitle(concertTitle)
                .concertScheduleId(1L)
                .playDateTime(LocalDateTime.of(2025, 1, 6, 23, 0, 0))
                .now(LocalDateTime.of(2025, 1, 6, 23, 0, 0))
                .seats(seats)
                .build();

        when(reservationRepository.saveAll(anyList()))
                .thenReturn(reservations);

        // when
        List<ReservationInfo> savedReservations = sut.createReservations(reservationCreateCommand);

        // then
        assertThat(savedReservations).hasSize(3);
        savedReservations.forEach(reservation -> assertThat(reservation).isNotNull());
    }

}