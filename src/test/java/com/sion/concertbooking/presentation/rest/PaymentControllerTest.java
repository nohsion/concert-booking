package com.sion.concertbooking.presentation.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sion.concertbooking.domain.dto.ReservationDto;
import com.sion.concertbooking.domain.enums.ReservationStatus;
import com.sion.concertbooking.domain.enums.SeatGrade;
import com.sion.concertbooking.domain.dto.UserPointDto;
import com.sion.concertbooking.presentation.request.ReservationCreateRequest;
import com.sion.concertbooking.presentation.response.PaymentResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();

    @DisplayName("결제 성공시 예약 정보와 차감 완료된 포인트를 반환한다.")
    @Test
    void processPayment() throws Exception {
        // given
        long userId = 1L;
        int point = 1000;

        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 3, 0, 0);

        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(1L, 1L, List.of(10L, 11L));
        String requestJson = mapper.writeValueAsString(reservationCreateRequest);

        PaymentResponse paymentResponse = new PaymentResponse(
                new UserPointDto(userId, point, dateTime),
                List.of(
                        new ReservationDto(1L, 1L, "지킬앤하이드", 1L, dateTime,
                                10L, 10, SeatGrade.VIP, 100_000, ReservationStatus.SUCCESS),
                        new ReservationDto(2L, 1L, "지킬앤하이드", 1L, dateTime,
                                11L, 11, SeatGrade.VIP, 100_000, ReservationStatus.SUCCESS)
                )
        );

        // when

        // then
        mockMvc.perform(post("/api/v1/payment")
                        .param("tokenId", "token")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String responseJson = result.getResponse().getContentAsString();
                    PaymentResponse response = mapper.readValue(responseJson, PaymentResponse.class);
                    assertThat(response).usingRecursiveComparison().isEqualTo(paymentResponse);
                });
    }

}