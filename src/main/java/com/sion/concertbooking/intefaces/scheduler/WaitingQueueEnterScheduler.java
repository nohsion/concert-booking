package com.sion.concertbooking.intefaces.scheduler;

import com.sion.concertbooking.domain.policy.ReservationEnterPolicy;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueInfo;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueService;
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

    private final ReservationEnterPolicy reservationEnterPolicy;
    private final WaitingQueueService waitingQueueService;

    public WaitingQueueEnterScheduler(
            final ReservationEnterPolicy reservationEnterPolicy,
            final WaitingQueueService waitingQueueService
    ) {
        this.reservationEnterPolicy = reservationEnterPolicy;
        this.waitingQueueService = waitingQueueService;
    }

    @Scheduled(cron = "* * * * * *")
    public void enterWaitingTokens() {
        LocalDateTime now = LocalDateTime.now();

        List<WaitingQueueInfo> enteredTokens = waitingQueueService.getEnteredTokens(now);
        reservationEnterPolicy.validateIsAvailableForEntry(enteredTokens.size());

        List<WaitingQueueInfo> waitingTokens = waitingQueueService.getWaitingTokens(now);
        if (waitingTokens.isEmpty()) {
            log.debug("대기중인 토큰이 없습니다.");
            return;
        }

        int limit = reservationEnterPolicy.getMaxLimitToEnter(enteredTokens.size(), waitingTokens.size());
        List<String> tokensToEnter = waitingTokens.subList(0, limit).stream()
                .map(WaitingQueueInfo::tokenId)
                .toList();

        int enteredCount = waitingQueueService.enterWaitingTokens(tokensToEnter);
        log.info("{}명 입장!", enteredCount);
    }

}
