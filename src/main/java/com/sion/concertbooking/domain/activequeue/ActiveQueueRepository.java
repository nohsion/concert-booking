package com.sion.concertbooking.domain.activequeue;

import java.time.LocalDateTime;
import java.util.List;

public interface ActiveQueueRepository {

    // queue
    String save(ActiveQueue waitingQueue);

    Long findRank(String tokenId, long concertId);

    Long deleteExpiredTokens(long concertId, LocalDateTime now);

    List<String> getActiveTokens(long concertId);

    // concert
    List<Long> getActiveConcerts();

    void addActiveConcert(long concertId);

    void removeActiveConcert(long concertId);

}
