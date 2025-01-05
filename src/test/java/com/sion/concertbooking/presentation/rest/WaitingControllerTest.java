package com.sion.concertbooking.presentation.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sion.concertbooking.presentation.request.WaitingQueueRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WaitingController.class)
class WaitingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();

    @DisplayName("대기열 등록시 토큰을 발급한다.")
    @Test
    void waitQueueAndIssueToken() throws Exception {
        // given
        long userId = 1L;
        long concertId = 1L;

        WaitingQueueRegisterRequest waitingQueueRegisterRequest = new WaitingQueueRegisterRequest(userId, concertId);
        String requestJson = mapper.writeValueAsString(waitingQueueRegisterRequest);

        // when

        // then
        mockMvc.perform(post("/api/v1/waiting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.concertId").value(concertId));
    }

    @DisplayName("대기열 정보 조회시 남은 순서와 예상 대기 시간을 반환한다.")
    @Test
    void getQueueByToken() throws Exception {
        // given
        int remainingWaitingOrder = 10;
        int remainingWaitingSec = 50;
        String tokenId = "token-id";

        // when

        // then
        mockMvc.perform(get("/api/v1/waiting")
                        .param("tokenId", tokenId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.tokenId").value(tokenId))
                .andExpect(jsonPath("$.remainingWaitingOrder").value(remainingWaitingOrder))
                .andExpect(jsonPath("$.remainingWaitingSec").value(remainingWaitingSec));
    }
}