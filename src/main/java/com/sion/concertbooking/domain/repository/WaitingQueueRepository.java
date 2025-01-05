package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.entity.WaitingQueue;

public interface WaitingQueueRepository {

    WaitingQueue save(WaitingQueue waitingQueue);

    WaitingQueue findByTokenId(String tokenId);
}
