package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.watingqueue.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WaitingQueueServiceTest {

    private WaitingQueueService sut;

    private WaitingQueueRepository waitingQueueRepository = mock(WaitingQueueRepository.class);

    @BeforeEach
    void setUp() {
        sut = new WaitingQueueService(waitingQueueRepository);
    }

    @DisplayName("토큰을 발급하고 대기열에 추가한다.")
    @Test
    void waitQueueAndIssueToken() {
        // given
        long userId = 1L;
        long concertId = 1L;
        String tokenId = "tokenId";
        LocalDateTime now = LocalDateTime.now();
        WaitingQueueIssueCommand issueCommand = new WaitingQueueIssueCommand(tokenId, userId, concertId, now);

        when(waitingQueueRepository.save(any(WaitingQueue.class)))
                .thenReturn(tokenId);

        // when
        String result = sut.waitQueue(issueCommand);

        // then
        assertThat(result).isEqualTo(tokenId);
    }

}