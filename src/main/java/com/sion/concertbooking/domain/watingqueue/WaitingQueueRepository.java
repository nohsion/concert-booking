package com.sion.concertbooking.domain.watingqueue;

import java.util.List;

public interface WaitingQueueRepository {

    WaitingQueue save(WaitingQueue waitingQueue);

    WaitingQueue findByTokenId(String tokenId);

    List<WaitingQueue> getWaitingStatusTokens();

    int updateStatusInBatch(List<String> tokens, WaitingQueueStatus status);
}
