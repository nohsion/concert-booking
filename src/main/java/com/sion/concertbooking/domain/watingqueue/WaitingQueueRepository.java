package com.sion.concertbooking.domain.watingqueue;

import java.util.List;

public interface WaitingQueueRepository {

    WaitingQueue save(WaitingQueue waitingQueue);

    WaitingQueue findByTokenId(String tokenId);

    List<WaitingQueue> findByWaitingStatus();

    List<WaitingQueue> findByEnteredStatus();

    int updateStatusInBatch(List<String> tokens, WaitingQueueStatus status);
}
