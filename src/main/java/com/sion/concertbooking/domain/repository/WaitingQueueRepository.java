package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.entity.WaitingQueue;
import com.sion.concertbooking.domain.enums.WaitingQueueStatus;

import java.util.List;

public interface WaitingQueueRepository {

    WaitingQueue save(WaitingQueue waitingQueue);

    WaitingQueue findByTokenId(String tokenId);

    List<WaitingQueue> getWaitingStatusTokens();

    int updateStatusInBatch(List<String> tokens, WaitingQueueStatus status);
}
