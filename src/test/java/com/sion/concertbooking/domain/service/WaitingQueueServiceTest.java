package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.dto.WaitingQueueDto;
import com.sion.concertbooking.domain.dto.WaitingQueueInfo;
import com.sion.concertbooking.domain.entity.WaitingQueue;
import com.sion.concertbooking.domain.enums.WaitingQueueStatus;
import com.sion.concertbooking.domain.repository.WaitingQueueRepository;
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
    private TokenProvider tokenProvider = mock(TokenProvider.class);

    @BeforeEach
    void setUp() {
        sut = new WaitingQueueService(waitingQueueRepository, tokenProvider);
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
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.WAITING)
                .set(field(WaitingQueue::getCreatedAt), now)
                .set(field(WaitingQueue::getExpiredAt), now.plusMinutes(10))
                .create();

        when(tokenProvider.generateToken())
                .thenReturn(tokenId);
        when(waitingQueueRepository.save(any(WaitingQueue.class)))
                .thenReturn(waitingQueue);

        // when
        WaitingQueueDto result = sut.waitQueueAndIssueToken(userId, concertId, now);

        // then
        assertThat(result.waitingQueueId()).isEqualTo(1L);
        assertThat(result.tokenId()).isEqualTo(tokenId);
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.concertId()).isEqualTo(concertId);
        assertThat(result.createdAt()).isEqualTo(now);
        assertThat(result.status()).isEqualTo(WaitingQueueStatus.WAITING);
        assertThat(result.createdAt()).isEqualTo(now);
        assertThat(result.expiredAt()).isEqualTo(now.plusMinutes(10));
    }

    @DisplayName("대기열 정보 조회시 스케쥴러에 의해 입장한 경우, 남은 순서와 대기 시간은 0으로 반환한다.")
    @Test
    void getQueueInfoByTokenIdEntered() {
        // given
        String tokenId = "tokenId";
        WaitingQueue waitingQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getId), 1L)
                .set(field(WaitingQueue::getTokenId), tokenId)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.ENTERED)
                .create();

        when(waitingQueueRepository.findByTokenId(tokenId))
                .thenReturn(waitingQueue);

        // when
        WaitingQueueInfo result = sut.getQueueInfoByTokenId(tokenId);

        // then
        assertThat(result.tokenId()).isEqualTo(tokenId);
        assertThat(result.remainingWaitingOrder()).isZero();
        assertThat(result.remainingWaitingSec()).isZero();
    }

}