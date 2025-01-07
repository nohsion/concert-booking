package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.model.entity.WaitingQueue;
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

    @DisplayName("현재 대기중이면서 만료되지 않은 WaitingQueue만 조회한다.")
    @Test
    void findWaitingQueueTest() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        List<WaitingQueue> waitingQueues = Instancio.ofList(WaitingQueue.class).size(3)
                .set(field(WaitingQueue::getId), null)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.WAITING)
                .set(field(WaitingQueue::getExpiredAt), now.minusMinutes(5))
                .create();
        List<WaitingQueue> enteredQueues = Instancio.ofList(WaitingQueue.class).size(2)
                .set(field(WaitingQueue::getId), null)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.ENTERED)
                .create();
        waitingQueueJpaRepository.saveAll(waitingQueues);
        waitingQueueJpaRepository.saveAll(enteredQueues);

        // when
        List<WaitingQueue> result = waitingQueueRepository.findWaitingQueue(now);

        // then
        assertThat(result).hasSize(3);
    }

    @DisplayName("현재 대기중이지만 만료된 WaitingQueue는 조회하지 않는다.")
    @Test
    void findWaitingQueueExpiredTest() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        List<WaitingQueue> waitingQueues = Instancio.ofList(WaitingQueue.class).size(3)
                .set(field(WaitingQueue::getId), null)
                .set(field(WaitingQueue::getStatus), WaitingQueueStatus.WAITING)
                .set(field(WaitingQueue::getExpiredAt), now.plusMinutes(5))
                .create();
        waitingQueueJpaRepository.saveAll(waitingQueues);

        // when
        List<WaitingQueue> result = waitingQueueRepository.findWaitingQueue(now);

        // then
        assertThat(result).isEmpty();
    }

}