package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.watingqueue.*;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
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
        WaitingQueue waitingQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getId), 1L)
                .set(field(WaitingQueue::getTokenId), tokenId)
                .set(field(WaitingQueue::getUserId), userId)
                .set(field(WaitingQueue::getConcertId), concertId)
                .set(field(WaitingQueue::getStatus), WaitingQueue.Status.WAITING)
                .set(field(WaitingQueue::getCreatedAt), now)
                .set(field(WaitingQueue::getExpiredAt), now.plusMinutes(10))
                .create();
        WaitingQueueIssueCommand issueCommand = new WaitingQueueIssueCommand(tokenId, userId, concertId, now);

        when(waitingQueueRepository.save(any(WaitingQueue.class)))
                .thenReturn(waitingQueue);

        // when
        WaitingQueueInfo result = sut.waitQueueAndIssueToken(issueCommand);

        // then
        assertThat(result.waitingQueueId()).isEqualTo(1L);
        assertThat(result.tokenId()).isEqualTo(tokenId);
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.concertId()).isEqualTo(concertId);
        assertThat(result.createdAt()).isEqualTo(now);
        assertThat(result.status()).isEqualTo(WaitingQueue.Status.WAITING);
        assertThat(result.createdAt()).isEqualTo(now);
        assertThat(result.expiredAt()).isEqualTo(now.plusMinutes(10));
    }

}