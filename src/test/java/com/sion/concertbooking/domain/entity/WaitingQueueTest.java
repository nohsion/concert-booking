package com.sion.concertbooking.domain.entity;

import com.sion.concertbooking.domain.enums.WaitingQueueStatus;
import com.sion.concertbooking.domain.model.entity.WaitingQueue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class WaitingQueueTest {

    @DisplayName("대기열 큐 객체 생성시 초기 상태는 WAITING이여야 하고, 만료시간은 현재시간 기준 10분 후여야 한다.")
    @Test
    void of() {
        // given
        long userId = 1L;
        long concertId = 1L;
        String tokenId = "tokenId";
        LocalDateTime now = LocalDateTime.of(2025, 1, 5, 18, 10, 0);

        // when
        WaitingQueue waitingQueue = WaitingQueue.of(tokenId, userId, concertId, now);

        // then
        assertThat(waitingQueue.getStatus()).isEqualTo(WaitingQueueStatus.WAITING);
        assertThat(waitingQueue.getExpiredAt()).isEqualTo(now.plusMinutes(10));
    }
}