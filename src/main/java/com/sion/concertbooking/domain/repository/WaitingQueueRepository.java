package com.sion.concertbooking.domain.repository;

import com.sion.concertbooking.domain.model.entity.WaitingQueue;

import java.time.LocalDateTime;
import java.util.List;

public interface WaitingQueueRepository {

    WaitingQueue save(WaitingQueue waitingQueue);

    WaitingQueue findByTokenId(String tokenId);

    List<WaitingQueue> findWaitingQueue(LocalDateTime now);
}
