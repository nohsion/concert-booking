package com.sion.concertbooking.presentation.rest;

import com.sion.concertbooking.application.PaymentType;
import com.sion.concertbooking.application.PointCharger;
import com.sion.concertbooking.application.PointDeductor;
import com.sion.concertbooking.domain.model.info.PointInfo;
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

        long pointId = 1L;
        long userId = 1L;
        int point = 1000;
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 3, 0, 0);

        PointInfo pointInfo = new PointInfo(pointId, userId, point, updatedAt);
        when(pointCharger.chargePoint(userId, amountToCharge, PaymentType.FREE)).thenReturn(pointInfo);

        // when
        // then
        mockMvc.perform(post("/api/v1/point/charge")
                        .param("userId", String.valueOf(userId))
                        .param("amount", String.valueOf(amountToCharge))
                        .param("paymentType", "FREE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.point").value(point))
                .andExpect(jsonPath("$.updatedAt").value(
                        updatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

    @DisplayName("포인트 사용시 사용 완료된 포인트를 반환한다.")
    @Test
    void usePoint() throws Exception {
        // given
        String tokenId = "token";
        int amountToUse = 1000;

        long pointId = 1L;
        long userId = 1L;
        int point = 1000;
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 3, 0, 0);

        PointInfo pointInfo = new PointInfo(pointId, userId, point, updatedAt);
        when(pointDeductor.usePoint(userId, point)).thenReturn(pointInfo);

        // when
        // then
        mockMvc.perform(post("/api/v1/point/use")
                        .param("userId", String.valueOf(userId))
                        .param("tokenId", tokenId)
                        .param("amount", String.valueOf(amountToUse)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.point").value(point))
                .andExpect(jsonPath("$.updatedAt").value(
                        updatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

}