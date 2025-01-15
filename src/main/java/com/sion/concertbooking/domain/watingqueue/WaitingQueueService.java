package com.sion.concertbooking.domain.watingqueue;

import com.sion.concertbooking.domain.token.TokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WaitingQueueService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final TokenProvider tokenProvider;

    public WaitingQueueService(
            WaitingQueueRepository waitingQueueRepository,
            TokenProvider tokenProvider
    ) {
        this.waitingQueueRepository = waitingQueueRepository;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public WaitingQueueInfo waitQueueAndIssueToken(long userId, long concertId, LocalDateTime now) {
        String tokenId = tokenProvider.generateToken();
        WaitingQueue waitingQueue = WaitingQueue.of(tokenId, userId, concertId, now);
        WaitingQueue savedEntity = waitingQueueRepository.save(waitingQueue);

        return WaitingQueueInfo.fromEntity(savedEntity);
    }

    @Transactional
    public WaitingQueueInfo getQueueByTokenId(String tokenId) {
        WaitingQueue waitingQueue = waitingQueueRepository.findByTokenId(tokenId);
        if (waitingQueue == null) {
            throw new NoSuchElementException("해당 토큰은 대기열에 존재하지 않습니다. tokenId=" + tokenId);
        }
        return WaitingQueueInfo.fromEntity(waitingQueue);
    }

    @Transactional
    public WaitingQueueDetailInfo getQueueDetailByTokenId(String tokenId, LocalDateTime now) {
        WaitingQueue waitingQueue = waitingQueueRepository.findByTokenId(tokenId);
        if (waitingQueue == null || !waitingQueue.isTokenValid(now)) {
            throw new IllegalArgumentException("Invalid token");
        }
        // 입장한 경우
        if (WaitingQueueStatus.ENTERED == waitingQueue.getStatus()) {
            return new WaitingQueueDetailInfo(waitingQueue.getTokenId(), 0, 0);
        }
        // 대기중인 경우
        List<WaitingQueue> waitingQueues = waitingQueueRepository.getWaitingStatusTokens().stream()
                .filter(queue -> !queue.isExpiredTime(now))
                .toList();
        int remainingWaitingOrder = waitingQueues.indexOf(waitingQueue) + 1;
        int remainingTimeSec = 10; // TODO: 남은 대기 시간 계산.. 10초로 임시 설정

        return new WaitingQueueDetailInfo(waitingQueue.getTokenId(), remainingWaitingOrder, remainingTimeSec);
    }

    public boolean isTokenValid(String tokenId, LocalDateTime now) {
        WaitingQueue waitingQueue = waitingQueueRepository.findByTokenId(tokenId);
        if (waitingQueue == null) {
            return false;
        }
        return waitingQueue.isTokenValid(now);
    }

    @Transactional
    public void expireToken(String tokenId) {
        WaitingQueue waitingQueue = waitingQueueRepository.findByTokenId(tokenId);
        if (waitingQueue == null) {
            throw new NoSuchElementException("해당 토큰은 대기열에 존재하지 않습니다. tokenId=" + tokenId);
        }
        waitingQueue.updateStatusExpired();
    }

    /**
     * 현재 시간 기준으로 대기중인 토큰들을 반환한다.
     */
    public List<WaitingQueueInfo> getWaitingTokens(LocalDateTime now) {
        return waitingQueueRepository.getWaitingStatusTokens().stream()
                .filter(queue -> !queue.isExpiredTime(now))
                .map(WaitingQueueInfo::fromEntity)
                .toList();
    }

    /**
     * 현재 시간 기준으로 만료 처리시켜야 할 토큰들을 반환한다.
     */
    public List<WaitingQueueInfo> getWaitingTokensToExpire(LocalDateTime now) {
        return waitingQueueRepository.getWaitingStatusTokens().stream()
                .filter(queue -> queue.isExpiredTime(now))
                .map(WaitingQueueInfo::fromEntity)
                .toList();
    }

    /**
     * 현재 시간 기준으로 입장된 토큰들을 반환한다.
     */
    public List<WaitingQueueInfo> getEnteredTokens(LocalDateTime now) {
        return waitingQueueRepository.getWaitingStatusTokens().stream()
                .filter(queue -> queue.isEntered(now))
                .map(WaitingQueueInfo::fromEntity)
                .toList();
    }

    @Transactional
    public int enterWaitingTokens(List<String> tokenIds) {
        return waitingQueueRepository.updateStatusInBatch(tokenIds, WaitingQueueStatus.ENTERED);
    }

    @Transactional
    public int expireWaitingTokens(List<String> tokenIds) {
        return waitingQueueRepository.updateStatusInBatch(tokenIds, WaitingQueueStatus.EXPIRED);
    }
}
