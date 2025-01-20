package com.sion.concertbooking.presentation.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sion.concertbooking.application.reservation.ConcertReservationFacade;
import com.sion.concertbooking.application.reservation.ReservationCriteria;
import com.sion.concertbooking.application.reservation.ReservationResult;
import com.sion.concertbooking.domain.reservation.Reservation;
import com.sion.concertbooking.domain.seat.Seat;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueStatus;
import com.sion.concertbooking.intefaces.aspect.TokenInfo;
import com.sion.concertbooking.intefaces.presentation.request.ConcertReservationCreateRequest;
import com.sion.concertbooking.intefaces.presentation.rest.ReservationController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConcertReservationFacade concertReservationFacade;

    private final ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();


    @DisplayName("콘서트 좌석 예약시 성공적으로 예약된 좌석들을 모두 반환한다.")
    @Test
    void createReservations() throws Exception {
        // given
        long concertId = 1L;
        long concertScheduleId = 1L;
        long userId = 1L;
        String concertTitle = "지킬앤하이드";
        List<Long> seatIds = List.of(10L, 11L);
        LocalDateTime playDateTime = LocalDateTime.of(2025, 1, 3, 0, 0);

        ConcertReservationCreateRequest concertReservationCreateRequest = new ConcertReservationCreateRequest(concertId, concertScheduleId, seatIds);
        String requestJson = mapper.writeValueAsString(concertReservationCreateRequest);
        ReservationCriteria reservationCriteria = new ReservationCriteria(userId, concertId, concertScheduleId, seatIds);

        List<ReservationResult> reservationResults = List.of(
                new ReservationResult(1L, userId, concertId, concertTitle, concertScheduleId, playDateTime,
                        10L, 10, Seat.Grade.VIP, 100_000, Reservation.Status.SUCCESS),
                new ReservationResult(2L, userId, concertId, concertTitle, concertScheduleId, playDateTime,
                        11L, 11, Seat.Grade.VIP, 100_000, Reservation.Status.SUCCESS)
        );
        when(concertReservationFacade.reserve(reservationCriteria))
                .thenReturn(reservationResults);

        TokenInfo tokenInfo = new TokenInfo("token-id", 1L, 1L, WaitingQueueStatus.WAITING, LocalDateTime.now());

        // when
        // then
        mockMvc.perform(post("/api/v1/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .requestAttr("tokenInfo", tokenInfo)) // TokenInfo attribute 추가
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].reservationId").value(1L))
                .andExpect(jsonPath("$[0].concertScheduleId").value(concertScheduleId))
                .andExpect(jsonPath("$[0].playDateTime").value(
                        playDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .andExpect(jsonPath("$[0].seatId").value(10L))
                .andExpect(jsonPath("$[0].seatNum").value(10))
                .andExpect(jsonPath("$[0].seatGrade").value(Seat.Grade.VIP.name()))
                .andExpect(jsonPath("$[0].seatPrice").value(100_000))
                .andExpect(jsonPath("$[0].reservationStatus").value(Reservation.Status.SUCCESS.name()))
                .andExpect(jsonPath("$[1].reservationId").value(2L))
                .andExpect(jsonPath("$[1].concertScheduleId").value(concertScheduleId))
                .andExpect(jsonPath("$[1].playDateTime").value(
                        playDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .andExpect(jsonPath("$[1].seatId").value(11L))
                .andExpect(jsonPath("$[1].seatNum").value(11))
                .andExpect(jsonPath("$[1].seatGrade").value(Seat.Grade.VIP.name()))
                .andExpect(jsonPath("$[1].seatPrice").value(100_000))
                .andExpect(jsonPath("$[1].reservationStatus").value(Reservation.Status.SUCCESS.name()));

    }

}