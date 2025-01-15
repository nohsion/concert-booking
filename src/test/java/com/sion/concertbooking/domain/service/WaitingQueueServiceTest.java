package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.watingqueue.WaitingQueueInfo;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueDetailInfo;
import com.sion.concertbooking.domain.token.TokenProvider;
import com.sion.concertbooking.domain.watingqueue.WaitingQueue;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueStatus;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueRepository;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        WaitingQueueInfo result = sut.waitQueueAndIssueToken(userId, concertId, now);

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

    @DisplayName("대기열 상세정보 조회시 대기 상태인 경우, 남은 순서와 대기 시간은 0으로 반환한다.")
    @Test
    void getQueueDetailByTokenIdEntered() {
        // given
        LocalDateTime now = LocalDateTime.now();
        String tokenId = "tokenId";
        WaitingQueue waitingQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getId), 1L)
                .set(field(WaitingQueue::getTokenId), tokenId)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.ENTERED)
                .set(field(WaitingQueue::getExpiredAt), now.plusMinutes(10))
                .create();

        when(waitingQueueRepository.findByTokenId(tokenId))
                .thenReturn(waitingQueue);

        // when
        WaitingQueueDetailInfo result = sut.getQueueDetailByTokenId(tokenId, now);

        // then
        assertThat(result.tokenId()).isEqualTo(tokenId);
        assertThat(result.remainingWaitingOrder()).isZero();
        assertThat(result.remainingWaitingSec()).isZero();
    }

    @DisplayName("대기열 상세정보 조회시 토큰이 유효하지 않다면 IllegalArgumentException이 발생한다.")
    @Test
    void getQueueDetailByTokenIdExpiredTime() {
        // given
        LocalDateTime now = LocalDateTime.now();
        String tokenId = "tokenId";
        WaitingQueue waitingQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getId), 1L)
                .set(field(WaitingQueue::getTokenId), tokenId)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.EXPIRED)
                .create();

        when(waitingQueueRepository.findByTokenId(tokenId))
                .thenReturn(waitingQueue);

        // when & then
        assertThatThrownBy(() -> sut.getQueueDetailByTokenId(tokenId, now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid token");
    }

    @DisplayName("대기열 상세정보 조회시 대기 중인 경우, 남은 순서와 대기 시간을 반환한다.")
    @Test
    void getQueueDetailByTokenIdWaiting() {
        // given
        LocalDateTime now = LocalDateTime.now();
        String tokenId = "tokenId";
        WaitingQueue queue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getId), 1L)
                .set(field(WaitingQueue::getTokenId), tokenId)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.WAITING)
                .set(field(WaitingQueue::getExpiredAt), now.plusMinutes(10))
                .create();

        List<WaitingQueue> waitingQueues = Instancio.ofList(WaitingQueue.class).size(3)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.WAITING)
                .set(field(WaitingQueue::getExpiredAt), now.plusMinutes(10))
                .create();
        waitingQueues.add(queue); // 큐 마지막에 추가

        when(waitingQueueRepository.findByTokenId(tokenId))
                .thenReturn(queue);
        when(waitingQueueRepository.getWaitingStatusTokens())
                .thenReturn(waitingQueues);

        // when
        WaitingQueueDetailInfo result = sut.getQueueDetailByTokenId(tokenId, now);

        // then
        assertThat(result.tokenId()).isEqualTo(tokenId);
        assertThat(result.remainingWaitingOrder())
                .as("마지막 순번인 4번째 순서. 1번부터 시작하므로 4번이어야 한다.")
                .isEqualTo(4);
        assertThat(result.remainingWaitingSec()).isEqualTo(10); //TODO: 남은대기 시간 계산로직 완료되면 수정한다.
    }

}