package com.sion.concertbooking.presentation.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sion.concertbooking.presentation.response.PointResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();

    @DisplayName("포인트 충전시 충전 완료된 포인트를 반환한다.")
    @Test
    void chargePoint() throws Exception {
        // given
        String tokenId = "token";
        int amountToCharge = 1000;

        long userId = 1L;
        int point = 1000;
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 3, 0, 0);

        PointResponse pointResponse = new PointResponse(userId, point, updatedAt);

        // when
        // then
        mockMvc.perform(post("/api/v1/point/charge")
                        .param("tokenId", tokenId)
                        .param("amount", String.valueOf(amountToCharge)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String responseJson = result.getResponse().getContentAsString();
                    PointResponse response = mapper.readValue(responseJson, PointResponse.class);
                    assertThat(response).usingRecursiveComparison().isEqualTo(pointResponse);
                });
    }

    @DisplayName("포인트 사용시 사용 완료된 포인트를 반환한다.")
    @Test
    void usePoint() throws Exception {
        // given
        String tokenId = "token";
        int amountToUse = 1000;

        long userId = 1L;
        int point = 1000;
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 3, 0, 0);

        PointResponse pointResponse = new PointResponse(userId, point, updatedAt);

        // when
        // then
        mockMvc.perform(post("/api/v1/point/use")
                        .param("tokenId", tokenId)
                        .param("amount", String.valueOf(amountToUse)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    String responseJson = result.getResponse().getContentAsString();
                    PointResponse response = mapper.readValue(responseJson, PointResponse.class);
                    assertThat(response).usingRecursiveComparison().isEqualTo(pointResponse);
                });
    }

}