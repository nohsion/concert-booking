package com.sion.concertbooking.application.waitingqueue;

import com.sion.concertbooking.domain.waitingtoken.TokenProvider;
import com.sion.concertbooking.domain.waitingtoken.WaitingTokenInfo;
import com.sion.concertbooking.domain.waitingtoken.WaitingTokenService;
import com.sion.concertbooking.domain.watingqueue.*;
import org.springframework.stereotype.Component;

@Component
public class WaitingQueueFacade {

    private final WaitingQueueService waitingQueueService;
    private final WaitingTokenService waitingTokenService;
    private final TokenProvider tokenProvider;

    public WaitingQueueFacade(
            WaitingQueueService waitingQueueService,
            WaitingTokenService waitingTokenService,
            TokenProvider tokenProvider
    ) {
        this.waitingQueueService = waitingQueueService;
        this.waitingTokenService = waitingTokenService;
        this.tokenProvider = tokenProvider;
    }

    public WaitingQueueResult waitQueueAndIssueToken(WaitingQueueIssueCriteria criteria) {
        String tokenId = tokenProvider.generateToken();
        WaitingQueueIssueCommand issueCommand = new WaitingQueueIssueCommand(
                tokenId, criteria.userId(), criteria.concertId(), criteria.now()
        );
        WaitingTokenInfo savedToken = waitingTokenService.issue(issueCommand);
        waitingQueueService.waitQueue(issueCommand);
        return WaitingQueueResult.fromInfo(savedToken);
    }

    public WaitingQueueDetailResult getWaitingQueueDetail(WaitingQueueDetailCriteria criteria) {
        String tokenId = criteria.tokenId();
        long concertId = criteria.concertId();
        long remainingWaitingOrder = waitingQueueService.getRank(tokenId, concertId);

        // 입장 가능한 경우
        if (remainingWaitingOrder == 0) {
            return new WaitingQueueDetailResult(tokenId, concertId, 0, 0);
        }

        // 대기중인 경우
        int remainingTimeSec = 10;
        return new WaitingQueueDetailResult(tokenId, concertId, (int) remainingWaitingOrder, remainingTimeSec);
    }
}
