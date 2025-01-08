package com.sion.concertbooking.infrastructure.scheduler;

import com.sion.concertbooking.domain.info.WaitingQueueInfo;
import com.sion.concertbooking.domain.service.WaitingQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 만료시간이 지난 대기열 토큰을 주기적으로 만료시키는 스케줄러
 */
@Slf4j
@Component
public class WaitingQueueExpireScheduler {

    private final WaitingQueueService waitingQueueService;

    public WaitingQueueExpireScheduler(WaitingQueueService waitingQueueService) {
        this.waitingQueueService = waitingQueueService;
    }

    @Scheduled(cron = "* * * * * *")
    public void expireWaitingTokens() {
        LocalDateTime now = LocalDateTime.now();
        List<WaitingQueueInfo> waitingTokensToExpire = waitingQueueService.getWaitingTokensToExpire(now);
        if (waitingTokensToExpire.isEmpty()) {
            log.debug("만료시킬 토큰이 없습니다.");
            return;
        }
        List<String> tokensToExpire = waitingTokensToExpire.stream()
                .map(WaitingQueueInfo::tokenId)
                .toList();
        int expiredCount = waitingQueueService.expireWaitingTokens(tokensToExpire);
        log.info("{}명 만료!", expiredCount);
    }
}
