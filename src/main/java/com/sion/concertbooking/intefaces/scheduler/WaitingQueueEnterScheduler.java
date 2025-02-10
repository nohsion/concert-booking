package com.sion.concertbooking.intefaces.scheduler;

import com.sion.concertbooking.domain.activequeue.ActiveQueueService;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 대기열에서 활성열로 주기적으로 입장시키는 스케줄러
 */
@Slf4j
@Component
public class WaitingQueueEnterScheduler {

    private static final int ENTER_COUNT = 50;

    private final WaitingQueueService waitingQueueService;
    private final ActiveQueueService activeQueueService;

    public WaitingQueueEnterScheduler(
            final WaitingQueueService waitingQueueService,
            final ActiveQueueService activeQueueService
    ) {
        this.waitingQueueService = waitingQueueService;
        this.activeQueueService = activeQueueService;
    }

    @Scheduled(cron = "* * * * * *")
    public void enterWaitingTokens() {
        LocalDateTime now = LocalDateTime.now();

        List<Long> waitingConcertIds = waitingQueueService.getWaitingConcerts();
        for (Long concertId : waitingConcertIds) {
            List<String> waitingTokens = waitingQueueService.getWaitingTokens(concertId);
            if (CollectionUtils.isEmpty(waitingTokens)) {
                waitingQueueService.removeWaitingConcert(concertId);
                continue;
            }

            int enterCount = Math.min(ENTER_COUNT, waitingTokens.size());

            List<String> tokensToEnter = waitingTokens.subList(0, enterCount);
            // 대기열에서 삭제
            waitingQueueService.popMinByConcertId(enterCount, concertId);
            // 활성열에 추가
            activeQueueService.addTokens(tokensToEnter, concertId, now);

            log.info("concertId={}, {}명 입장!", concertId, enterCount);
        }
    }

}
