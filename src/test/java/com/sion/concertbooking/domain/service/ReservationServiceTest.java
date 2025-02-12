package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.reservation.ReservationCreateCommand;
import com.sion.concertbooking.domain.reservation.Reservation;
import com.sion.concertbooking.domain.reservation.ReservationInfo;
import com.sion.concertbooking.domain.reservation.ReservationRepository;
import com.sion.concertbooking.domain.reservation.ReservationService;
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