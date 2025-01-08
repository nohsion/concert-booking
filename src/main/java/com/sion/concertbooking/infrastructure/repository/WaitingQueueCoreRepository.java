package com.sion.concertbooking.infrastructure.repository;

import com.sion.concertbooking.domain.entity.WaitingQueue;
import com.sion.concertbooking.domain.enums.WaitingQueueStatus;
import com.sion.concertbooking.domain.repository.WaitingQueueRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class WaitingQueueCoreRepository implements WaitingQueueRepository {

    private final WaitingQueueJpaRepository waitingQueueJpaRepository;

    public WaitingQueueCoreRepository(WaitingQueueJpaRepository waitingQueueJpaRepository) {
        this.waitingQueueJpaRepository = waitingQueueJpaRepository;
    }

    @Override
    public WaitingQueue save(WaitingQueue waitingQueue) {
        return waitingQueueJpaRepository.save(waitingQueue);
    }

    @Override
    public WaitingQueue findByTokenId(final String tokenId) {
        return waitingQueueJpaRepository.findByTokenId(tokenId);
    }

    @Override
    public List<WaitingQueue> getWaitingStatusTokens() {
        return waitingQueueJpaRepository.findByStatusOrderById(WaitingQueueStatus.WAITING);
    }

    @Transactional
    @Override
    public int updateStatusInBatch(
            final List<String> tokens,
            final WaitingQueueStatus status
    ) {
        return waitingQueueJpaRepository.updateStatusInBatch(tokens, status);
    }
}
