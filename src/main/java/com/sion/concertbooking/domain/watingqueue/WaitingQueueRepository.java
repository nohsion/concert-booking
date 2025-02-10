package com.sion.concertbooking.domain.watingqueue;

import java.util.List;

public interface WaitingQueueRepository {

    // waiting queue
    String save(WaitingQueue waitingQueue);

    Long findRank(String tokenId, long concertId);

    void popMin(int count, long concertId);

    List<String> getWaitingTokens(long concertId);

    void removeToken(String tokenId, long concertId);

    // concert
    List<Long> getWaitingConcerts();

    void addWaitingConcert(long concertId);

    void removeWaitingConcert(long concertId);
}
