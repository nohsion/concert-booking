package com.sion.concertbooking.presentation.rest;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sion.concertbooking.domain.enums.ReservationStatus;
import com.sion.concertbooking.domain.enums.SeatGrade;
import com.sion.concertbooking.presentation.request.ConcertReservationCreateRequest;
import com.sion.concertbooking.presentation.response.ReservationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();


    @DisplayName("콘서트 좌석 예약시 성공적으로 예약된 좌석들을 모두 반환한다.")
    @Test
    void createReservations() throws Exception {
        // given
        long concertId = 1L;
        long concertScheduleId = 1L;
        List<Long> seatIds = List.of(10L, 11L);
        LocalDateTime playDateTime = LocalDateTime.of(2025, 1, 3, 0, 0);

        ConcertReservationCreateRequest concertReservationCreateRequest = new ConcertReservationCreateRequest(concertId, concertScheduleId, seatIds);
        String requestJson = mapper.writeValueAsString(concertReservationCreateRequest);

        // when
        List<ReservationResponse> reservationResponses = List.of(
                new ReservationResponse(
                        1L, 1L, "아리아나그란데 내한", 1L, playDateTime,
                        10L, 10, SeatGrade.VIP, 100_000, ReservationStatus.SUSPEND
                ),
                new ReservationResponse(
                        2L, 1L, "아리아나그란데 내한", 1L, playDateTime,
                        11L, 11, SeatGrade.VIP, 100_000, ReservationStatus.SUSPEND
                )
        );

        TypeReference<List<ReservationResponse>> typeReference = new TypeReference<>() {
        };

        // then
        mockMvc.perform(post("/api/v1/reservation")
                        .queryParam("tokenId", "token-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String responseJson = result.getResponse().getContentAsString();
                    List<ReservationResponse> actual = mapper.readValue(responseJson, typeReference);
                    assertThat(actual).usingRecursiveComparison().isEqualTo(reservationResponses);
                });
    }

}