package com.sion.concertbooking.domain.activequeue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ActiveQueueService {

    private final ActiveQueueRepository activeQueueRepository;

    public ActiveQueueService(
            ActiveQueueRepository activeQueueRepository
    ) {
        this.activeQueueRepository = activeQueueRepository;
    }

    public void addTokens(List<String> tokensToEnter, Long concertId, LocalDateTime now) {
        tokensToEnter.forEach(token -> {
            ActiveQueue activeQueue = ActiveQueue.of(token, concertId, now);
            // 활성큐 저장
            activeQueueRepository.save(activeQueue);
            // 활성 콘서트 추가
            activeQueueRepository.addActiveConcert(concertId);
        });
    }

    public List<String> getActiveTokens(long concertId) {
        return activeQueueRepository.getActiveTokens(concertId);
    }

    public Long deleteExpiredTokens(long concertId, LocalDateTime now) {
        return activeQueueRepository.deleteExpiredTokens(concertId, now);
    }

    public List<Long> getActiveConcerts() {
        return activeQueueRepository.getActiveConcerts();
    }
}
