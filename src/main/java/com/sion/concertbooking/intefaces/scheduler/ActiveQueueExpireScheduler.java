package com.sion.concertbooking.intefaces.scheduler;

import com.sion.concertbooking.domain.activequeue.ActiveQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 만료시간이 지난 활성열에 존재하는 토큰을 주기적으로 만료시키는 스케줄러
 */
@Slf4j
@Component
public class ActiveQueueExpireScheduler {

    private final ActiveQueueService activeQueueService;

    public ActiveQueueExpireScheduler(ActiveQueueService activeQueueService) {
        this.activeQueueService = activeQueueService;
    }

    @Scheduled(cron = "* * * * * *")
    public void expireWaitingTokens() {
        LocalDateTime now = LocalDateTime.now();

        List<Long> activeConcertIds = activeQueueService.getActiveConcerts();
        activeConcertIds.forEach(
                concertId -> {
                    Long expiredCount = activeQueueService.deleteExpiredTokens(concertId, now);
                    log.info("concertId={}, {}명 만료!", concertId, expiredCount);
                }
        );
    }
}
