package com.sion.concertbooking.infrastructure.scheduler;

import com.sion.concertbooking.domain.info.WaitingQueueInfo;
import com.sion.concertbooking.domain.service.WaitingQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 대기중인 토큰들을 주기적으로 입장시키는 스케줄러
 */
@Slf4j
@Component
public class WaitingQueueEnterScheduler {

    private static final int MAX_TOKENS_PER_ADMISSION = 100; // 한 번에 입장 가능한 인원 제한
    private static final int MAX_TOKENS_ENTERED = 10_000; // 최대 입장 가능한 인원 제한

    private final WaitingQueueService waitingQueueService;

    public WaitingQueueEnterScheduler(WaitingQueueService waitingQueueService) {
        this.waitingQueueService = waitingQueueService;
    }

    @Scheduled(cron = "* * * * * *")
    public void enterWaitingTokens() {
        LocalDateTime now = LocalDateTime.now();

        List<WaitingQueueInfo> enteredTokens = waitingQueueService.getEnteredTokens(now);
        if (enteredTokens.size() >= MAX_TOKENS_ENTERED) {
            log.info("현재 인원이 꽉차서 추가로 입장할 수 없습니다.");
            return;
        }

        List<WaitingQueueInfo> waitingTokens = waitingQueueService.getWaitingTokens(now);
        if (waitingTokens.isEmpty()) {
            log.debug("대기중인 토큰이 없습니다.");
            return;
        }

        // 대기중인 인원 중에서 한 번에 입장 가능한 인원수 만큼 차례로 입장시킨다.
        // 단, 최대 입장 가능한 인원 제한을 넘지 않도록 한다.
        int maxLimit = Math.min(MAX_TOKENS_ENTERED - enteredTokens.size(), MAX_TOKENS_PER_ADMISSION);
        int limit = Math.min(waitingTokens.size(), maxLimit);
        List<String> tokensToEnter = waitingTokens.subList(0, limit).stream()
                .map(WaitingQueueInfo::tokenId)
                .toList();

        int enteredCount = waitingQueueService.enterWaitingTokens(tokensToEnter);
        log.info("{}명 입장!", enteredCount);
    }

}
