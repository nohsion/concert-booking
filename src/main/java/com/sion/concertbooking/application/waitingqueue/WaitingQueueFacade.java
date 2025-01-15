package com.sion.concertbooking.application.waitingqueue;

import com.sion.concertbooking.domain.policy.ReservationEnterPolicy;
import com.sion.concertbooking.domain.policy.WaitingQueueRemainingPolicy;
import com.sion.concertbooking.domain.token.TokenProvider;
import com.sion.concertbooking.domain.watingqueue.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class WaitingQueueFacade {

    private final WaitingQueueService waitingQueueService;
    private final TokenProvider tokenProvider;
    private final WaitingQueueRemainingPolicy waitingQueueRemainingPolicy;
    private final ReservationEnterPolicy reservationEnterPolicy;

    public WaitingQueueFacade(
            WaitingQueueService waitingQueueService,
            TokenProvider tokenProvider,
            WaitingQueueRemainingPolicy waitingQueueRemainingPolicy,
            ReservationEnterPolicy reservationEnterPolicy
    ) {
        this.waitingQueueService = waitingQueueService;
        this.tokenProvider = tokenProvider;
        this.waitingQueueRemainingPolicy = waitingQueueRemainingPolicy;
        this.reservationEnterPolicy = reservationEnterPolicy;
    }

    public WaitingQueueResult waitQueueAndIssueToken(WaitingQueueIssueCriteria criteria) {
        String tokenId = tokenProvider.generateToken();
        WaitingQueueIssueCommand issueCommand = new WaitingQueueIssueCommand(
                tokenId, criteria.userId(), criteria.concertId(), criteria.now()
        );
        WaitingQueueInfo waitingQueueInfo = waitingQueueService.waitQueueAndIssueToken(issueCommand);
        return WaitingQueueResult.fromInfo(waitingQueueInfo);
    }

    public WaitingQueueDetailResult getWaitingQueueDetail(WaitingQueueDetailCriteria criteria) {
        LocalDateTime now = criteria.now();
        WaitingQueueInfo targetQueue = waitingQueueService.getQueueByTokenId(criteria.tokenId());

        // 입장 가능한 경우
        boolean isProcessing = waitingQueueService.isProcessing(targetQueue.tokenId(), now);
        if (isProcessing) {
            return new WaitingQueueDetailResult(targetQueue.tokenId(), 0, 0);
        }

        // 대기중인 경우
        int processingCapacity = reservationEnterPolicy.getMaxTokensEntered();
        List<WaitingQueueInfo> waitingQueues = waitingQueueService.getWaitingTokens(now);
        List<WaitingQueueInfo> processingQueues = waitingQueueService.getProcessingTokens(now);

        int remainingWaitingOrder = waitingQueueRemainingPolicy.calculateRemainingWaitingOrder(
                targetQueue, waitingQueues
        );
        int remainingTimeSec = waitingQueueRemainingPolicy.calculateRemainingTimeSec(
                targetQueue, waitingQueues, processingQueues, processingCapacity
        );
        return new WaitingQueueDetailResult(targetQueue.tokenId(), remainingWaitingOrder, remainingTimeSec);
    }
}
