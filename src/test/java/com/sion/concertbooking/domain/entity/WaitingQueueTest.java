package com.sion.concertbooking.domain.entity;

import com.sion.concertbooking.domain.watingqueue.WaitingQueue;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

class WaitingQueueTest {

    @DisplayName("대기열 큐 객체 생성시 초기 상태는 WAITING이여야 하고, 만료시간은 현재시간 기준 10분 후여야 한다.")
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
        assertThat(waitingQueue.getStatus()).isEqualTo(WaitingQueue.Status.WAITING);
        assertThat(waitingQueue.getExpiredAt()).isEqualTo(now.plusMinutes(10));
    }

    @DisplayName("대기열 큐의 상태를 EXPIRED로 변경한다.")
    @Test
    void updateStatusExpiredTest() {
        // given
        WaitingQueue waitingQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getStatus), WaitingQueue.Status.WAITING)
                .create();

        // when
        waitingQueue.updateStatusExpired();

        // then
        assertThat(waitingQueue.getStatus()).isEqualTo(WaitingQueue.Status.EXPIRED);
    }

    @DisplayName("WAITING 상태이지만 만료시간이 지났다면 isExpiredTime()은 true를 반환한다.")
    @Test
    void isExpiredTimeWhenStatusIsWaiting() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 5, 18, 10, 0);
        WaitingQueue waitingQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getStatus), WaitingQueue.Status.WAITING)
                .set(field(WaitingQueue::getExpiredAt), now.minusMinutes(1))
                .create();

        // when
        boolean result = waitingQueue.isExpiredTime(now);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("EXPIRED 상태이지만 만료시간이 지나지 않았다면 isExpiredTime()은 false를 반환한다.")
    @Test
    void isExpiredTimeWhenStatusIsExpired() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 5, 18, 10, 0);
        WaitingQueue waitingQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getStatus), WaitingQueue.Status.EXPIRED)
                .set(field(WaitingQueue::getExpiredAt), now.plusMinutes(1))
                .create();

        // when
        boolean result = waitingQueue.isExpiredTime(now);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("WAITING 혹은 ENTERED 상태이면서 만료시간이 지나지 않았다면 isTokenValid()은 true를 반환한다.")
    @Test
    void isTokenValidWhenStatusIsWaiting() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 5, 18, 10, 0);
        WaitingQueue waitingQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getStatus), WaitingQueue.Status.WAITING)
                .set(field(WaitingQueue::getExpiredAt), now.plusMinutes(1))
                .create();
        WaitingQueue enteredQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getStatus), WaitingQueue.Status.ENTERED)
                .set(field(WaitingQueue::getExpiredAt), now.plusMinutes(1))
                .create();

        List.of(waitingQueue, enteredQueue).forEach(queue -> {
            // when
            boolean result = queue.isTokenValid(now);

            // then
            assertThat(result).isTrue();
        });
    }

    @DisplayName("WAITING 혹은 ENTERED 상태여도 만료시간이 지났다면 isTokenValid()은 false를 반환한다.")
    @Test
    void isTokenValidWhenStatusIsExpired() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 5, 18, 10, 0);
        WaitingQueue waitingQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getStatus), WaitingQueue.Status.WAITING)
                .set(field(WaitingQueue::getExpiredAt), now.minusMinutes(1))
                .create();
        WaitingQueue enteredQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getStatus), WaitingQueue.Status.ENTERED)
                .set(field(WaitingQueue::getExpiredAt), now.minusMinutes(1))
                .create();

        List.of(waitingQueue, enteredQueue).forEach(queue -> {
            // when
            boolean result = queue.isTokenValid(now);

            // then
            assertThat(result).isFalse();
        });
    }

    @DisplayName("EXPIRED 상태는 항상 isTokenValid()은 false를 반환한다.")
    @Test
    void isTokenValidWhenStatusIsProcessing() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 5, 18, 10, 0);
        WaitingQueue expiredStatusButNotExpiredTimeQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getStatus), WaitingQueue.Status.EXPIRED)
                .set(field(WaitingQueue::getExpiredAt), now.plusMinutes(1))
                .create();
        WaitingQueue expiredStatusAndExpiredTimeQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getStatus), WaitingQueue.Status.EXPIRED)
                .set(field(WaitingQueue::getExpiredAt), now.plusMinutes(1))
                .create();

        List.of(expiredStatusButNotExpiredTimeQueue, expiredStatusAndExpiredTimeQueue).forEach(queue -> {
            // when
            boolean result = queue.isTokenValid(now);

            // then
            assertThat(result).isFalse();
        });
    }

    @DisplayName("ENTERED 상태이고 만료시간이 지나지 않았다면 isEntered()은 true를 반환한다.")
    @Test
    void isEnteredWhenStatusIsProcessingAndNotExpiredTime() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 5, 18, 10, 0);
        WaitingQueue waitingQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getStatus), WaitingQueue.Status.ENTERED)
                .set(field(WaitingQueue::getExpiredAt), now.plusMinutes(1))
                .create();

        // when
        boolean result = waitingQueue.isProcessing(now);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("ENTERED 상태이지만 만료시간이 지났다면 isEntered()은 false를 반환한다.")
    @Test
    void isEnteredWhenStatusIsWaitingButExpiredTime() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 5, 18, 10, 0);
        WaitingQueue waitingQueue = Instancio.of(WaitingQueue.class)
                .set(field(WaitingQueue::getStatus), WaitingQueue.Status.ENTERED)
                .set(field(WaitingQueue::getExpiredAt), now.minusMinutes(1))
                .create();

        // when
        boolean result = waitingQueue.isProcessing(now);

        // then
        assertThat(result).isFalse();
    }
}