package com.sion.concertbooking.presentation.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sion.concertbooking.domain.model.info.WaitingQueueInfo;
import com.sion.concertbooking.domain.dto.WaitingQueueDetailInfo;
import com.sion.concertbooking.domain.enums.WaitingQueueStatus;
import com.sion.concertbooking.domain.service.WaitingQueueService;
import com.sion.concertbooking.infrastructure.aspect.TokenInfo;
import com.sion.concertbooking.presentation.request.WaitingQueueRegisterRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WaitingQueueController.class)
class WaitingQueueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WaitingQueueService waitingQueueService;

    private ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();

    @DisplayName("대기열 등록시 토큰을 발급한다.")
    @Test
    void waitQueueAndIssueToken() throws Exception {
        // given
        long userId = 1L;
        long concertId = 1L;
        LocalDateTime now = LocalDateTime.of(2025, 1, 5, 18, 10, 0);
        WaitingQueueInfo waitingQueueInfo = new WaitingQueueInfo(
                1L, "token-id", userId, concertId, WaitingQueueStatus.WAITING, now, now.plusMinutes(10)
        );

        WaitingQueueRegisterRequest waitingQueueRegisterRequest = new WaitingQueueRegisterRequest(userId, concertId);
        String requestJson = mapper.writeValueAsString(waitingQueueRegisterRequest);

        // when
        when(waitingQueueService.waitQueueAndIssueToken(eq(userId), eq(concertId), any(LocalDateTime.class)))
                .thenReturn(waitingQueueInfo);

        // then
        mockMvc.perform(post("/api/v1/waiting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.concertId").value(concertId))
                .andExpect(jsonPath("$.tokenId").value("token-id"))
                .andExpect(jsonPath("$.createdAt").value(
                        now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .andExpect(jsonPath("$.expiredAt").value(
                        now.plusMinutes(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

    @DisplayName("대기열 정보 조회시 남은 순서와 예상 대기 시간을 반환한다.")
    @Test
    void getQueueByToken() throws Exception {
        // given
        int remainingWaitingOrder = 10;
        int remainingWaitingSec = 50;
        String tokenId = "token-id";

        TokenInfo tokenInfo = new TokenInfo(tokenId, 1L, 1L, WaitingQueueStatus.WAITING, LocalDateTime.now());

        // when
        when(waitingQueueService.getQueueInfoByTokenId(tokenId))
                .thenReturn(new WaitingQueueDetailInfo(tokenId, remainingWaitingOrder, remainingWaitingSec));

        // then
        mockMvc.perform(get("/api/v1/waiting")
                        .param("tokenId", tokenId)
                        .requestAttr("tokenInfo", tokenInfo)) // TokenInfo attribute 추가
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tokenId").value(tokenId))
                .andExpect(jsonPath("$.remainingWaitingOrder").value(remainingWaitingOrder))
                .andExpect(jsonPath("$.remainingWaitingSec").value(remainingWaitingSec));
    }
}