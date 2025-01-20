package com.sion.concertbooking.presentation.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sion.concertbooking.application.payment.PaymentFacade;
import com.sion.concertbooking.application.payment.PaymentCriteria;
import com.sion.concertbooking.application.payment.PaymentResult;
import com.sion.concertbooking.domain.reservation.Reservation;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueStatus;
import com.sion.concertbooking.domain.reservation.ReservationInfo;
import com.sion.concertbooking.domain.seat.SeatGrade;
import com.sion.concertbooking.intefaces.aspect.TokenInfo;
import com.sion.concertbooking.intefaces.presentation.rest.PaymentController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentFacade paymentFacade;

    private final ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();

    @DisplayName("결제 성공시 예약 정보와 차감 완료된 포인트를 반환한다.")
    @Test
    void processPayment() throws Exception {
        // given
        String tokenId = "token";
        long concertId = 1L;
        long userId = 1L;
        int amountToUse = 1000;
        int balance = 9000;

        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 3, 0, 0);

        PaymentCriteria paymentCriteria = new PaymentCriteria(userId, tokenId, List.of(1L, 2L));
        String requestJson = mapper.writeValueAsString(paymentCriteria);

        List<ReservationInfo> reservations = List.of(
                new ReservationInfo(1L, userId, concertId, "지킬앤하이드", 1L, dateTime,
                        10L, 10, SeatGrade.VIP, 100_000, Reservation.Status.SUCCESS, dateTime),
                new ReservationInfo(2L, userId, concertId, "지킬앤하이드", 1L, dateTime,
                        11L, 11, SeatGrade.VIP, 100_000, Reservation.Status.SUCCESS, dateTime)
        );
        PaymentResult paymentResult = new PaymentResult(userId, amountToUse, balance, reservations);

        when(paymentFacade.processPayment(eq(paymentCriteria)))
                .thenReturn(paymentResult);

        TokenInfo tokenInfo = new TokenInfo(tokenId, userId, concertId, WaitingQueueStatus.ENTERED, LocalDateTime.now());

        // when
        // then
        mockMvc.perform(post("/api/v1/payment")
                        .param("tokenId", "token")
                        .contentType("application/json")
                        .content(requestJson)
                        .requestAttr("tokenInfo", tokenInfo)) // TokenInfo attribute 추가)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.pointAmount").value(amountToUse))
                .andExpect(jsonPath("$.pointBalance").value(balance))
                .andExpect(jsonPath("$.reservations").isArray())
                .andExpect(jsonPath("$.reservations[0].reservationId").value(1L))
                .andExpect(jsonPath("$.reservations[1].reservationId").value(2L));
    }

}