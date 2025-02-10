package com.sion.concertbooking.domain.entity;

import com.sion.concertbooking.domain.watingqueue.WaitingQueue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class WaitingQueueTest {

    @DisplayName("대기열 큐 객체 생성시 생성시간은 현재 시간으로 설정된다.")
    @Test
    void ofFactoryMethodTest() {
        // given
        long userId = 1L;
        long concertId = 1L;
        String tokenId = "tokenId";
        LocalDateTime now = LocalDateTime.of(2025, 1, 5, 18, 10, 0);

        // when
        WaitingQueue waitingQueue = WaitingQueue.of(tokenId, userId, concertId, now);

        // then
        assertThat(waitingQueue.getCreatedAt()).isEqualTo(now);
    }

}