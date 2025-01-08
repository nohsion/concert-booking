package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.entity.WaitingQueue;
import com.sion.concertbooking.domain.enums.WaitingQueueStatus;
import com.sion.concertbooking.domain.repository.WaitingQueueRepository;
import com.sion.concertbooking.test.TestDataCleaner;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@SpringBootTest
class WaitingQueueCoreRepositoryTest {

    @Autowired
    private WaitingQueueRepository waitingQueueRepository;

    @Autowired
    private WaitingQueueJpaRepository waitingQueueJpaRepository;

    @Autowired
    private TestDataCleaner testDataCleaner;

    @BeforeEach
    void setUp() {
        testDataCleaner.cleanUp();
    }

    @DisplayName("현재 WAITING 상태인 WaitingQueue만 조회한다.")
    @Test
    void findWaitingQueueStatusWaitingTest() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        List<WaitingQueue> waitingQueues = Instancio.ofList(WaitingQueue.class).size(3)
                .set(field(WaitingQueue::getId), null)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.WAITING)
                .set(field(WaitingQueue::getExpiredAt), now.minusMinutes(5)) // 만료시간이 지나지 않음
                .create();
        List<WaitingQueue> enteredQueues = Instancio.ofList(WaitingQueue.class).size(2)
                .set(field(WaitingQueue::getId), null)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.ENTERED)
                .create();
        List<WaitingQueue> expiredQueues = Instancio.ofList(WaitingQueue.class).size(2)
                .set(field(WaitingQueue::getId), null)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.EXPIRED)
                .create();
        waitingQueueJpaRepository.saveAll(waitingQueues);
        waitingQueueJpaRepository.saveAll(enteredQueues);
        waitingQueueJpaRepository.saveAll(expiredQueues);

        // when
        List<WaitingQueue> result = waitingQueueRepository.getWaitingStatusTokens();

        // then
        assertThat(result).hasSize(3);
    }

    @DisplayName("만료시간과 상관없이 WAITING 상태인 모든 WaitingQueue를 조회한다.") // 만료시간 체크는 Domain에서 처리한다.
    @Test
    void findWaitingQueueStatusWaitingButExpiredTimeTest() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        List<WaitingQueue> waitingQueues = Instancio.ofList(WaitingQueue.class).size(3)
                .set(field(WaitingQueue::getId), null)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.WAITING)
                .set(field(WaitingQueue::getExpiredAt), now.plusMinutes(5)) // 만료시간이 지남
                .create();
        List<WaitingQueue> enteredQueues = Instancio.ofList(WaitingQueue.class).size(2)
                .set(field(WaitingQueue::getId), null)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.ENTERED)
                .create();
        List<WaitingQueue> expiredQueues = Instancio.ofList(WaitingQueue.class).size(2)
                .set(field(WaitingQueue::getId), null)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.EXPIRED)
                .create();
        waitingQueueJpaRepository.saveAll(waitingQueues);
        waitingQueueJpaRepository.saveAll(enteredQueues);
        waitingQueueJpaRepository.saveAll(expiredQueues);

        // when
        List<WaitingQueue> result = waitingQueueRepository.getWaitingStatusTokens();

        // then
        assertThat(result).hasSize(3);
    }

    @DisplayName("100개의 WaitingQueue의 상태를 일괄 업데이트시킨다.")
    @Test
    void updateStatusInBatchSuccess() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        List<WaitingQueue> waitingQueues = Instancio.ofList(WaitingQueue.class).size(100)
                .set(field(WaitingQueue::getId), null)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.WAITING)
                .set(field(WaitingQueue::getExpiredAt), now.minusMinutes(5)) // 만료시간이 지나지 않음
                .create();

        List<String> tokens = waitingQueues.stream().map(WaitingQueue::getTokenId).toList();

        waitingQueueJpaRepository.saveAll(waitingQueues);

        // when
        int successCount = waitingQueueRepository.updateStatusInBatch(tokens, WaitingQueueStatus.ENTERED);

        // then
        assertThat(successCount).isEqualTo(100);

        List<WaitingQueue> savedResult = waitingQueueJpaRepository.findAll();
        assertThat(savedResult).hasSize(100)
                .allMatch(waitingQueue -> waitingQueue.getStatus() == WaitingQueueStatus.ENTERED);
    }

}