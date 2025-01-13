package com.sion.concertbooking.presentation.rest;

import com.sion.concertbooking.application.PaymentType;
import com.sion.concertbooking.application.PointCharger;
import com.sion.concertbooking.application.PointDeductor;
import com.sion.concertbooking.application.result.PointResult;
import com.sion.concertbooking.domain.pointhistory.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PointCharger pointCharger;

    @MockitoBean
    private PointDeductor pointDeductor;

    @DisplayName("포인트 충전시 충전 완료된 포인트를 반환한다.")
    @Test
    void chargePoint() throws Exception {
        // given
        int amountToCharge = 1000;
        long userId = 1L;
        int balance = 1000;
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 3, 0, 0);

        PointResult.Charge pointChargeResult = new PointResult.Charge(userId, amountToCharge, balance, PaymentType.FREE, updatedAt);
        when(pointCharger.chargePoint(userId, amountToCharge, PaymentType.FREE))
                .thenReturn(pointChargeResult);

        // when
        // then
        mockMvc.perform(post("/api/v1/point/charge")
                        .param("userId", String.valueOf(userId))
                        .param("amount", String.valueOf(amountToCharge))
                        .param("paymentType", "FREE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.amount").value(amountToCharge))
                .andExpect(jsonPath("$.balance").value(balance))
                .andExpect(jsonPath("$.transactionType").value(TransactionType.CHARGE.name()))
                .andExpect(jsonPath("$.paymentType").value(PaymentType.FREE.name()))
                .andExpect(jsonPath("$.updatedAt").value(
                        updatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

    @DisplayName("포인트 사용시 사용 완료된 포인트를 반환한다.")
    @Test
    void usePoint() throws Exception {
        // given
        int amountToUse = 1000;
        long userId = 1L;
        int balance = 1000;
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 3, 0, 0);

        PointResult.Use pointUseResult = new PointResult.Use(userId, amountToUse, balance, updatedAt);
        when(pointDeductor.usePoint(userId, amountToUse))
                .thenReturn(pointUseResult);

        // when
        // then
        mockMvc.perform(post("/api/v1/point/use")
                        .param("userId", String.valueOf(userId))
                        .param("amount", String.valueOf(amountToUse)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.amount").value(amountToUse))
                .andExpect(jsonPath("$.balance").value(balance))
                .andExpect(jsonPath("$.transactionType").value(TransactionType.USE.name()))
                .andExpect(jsonPath("$.updatedAt").value(
                        updatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

}