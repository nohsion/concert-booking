package com.sion.concertbooking.domain.watingqueue;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WaitingQueueService {

    private final WaitingQueueRepository waitingQueueRepository;

    public WaitingQueueService(WaitingQueueRepository waitingQueueRepository) {
        this.waitingQueueRepository = waitingQueueRepository;
    }

    @Transactional
    public WaitingQueueInfo waitQueueAndIssueToken(WaitingQueueIssueCommand command) {
        WaitingQueue waitingQueue = WaitingQueue.of(
                command.tokenId(), command.userId(), command.concertId(), command.now()
        );
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

    public boolean isProcessing(String tokenId, LocalDateTime now) {
        WaitingQueue waitingQueue = waitingQueueRepository.findByTokenId(tokenId);
        if (waitingQueue == null) {
            return false;
        }
        return waitingQueue.isProcessing(now);
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
        return waitingQueueRepository.findByWaitingStatus().stream()
                .filter(queue -> !queue.isExpiredTime(now))
                .map(WaitingQueueInfo::fromEntity)
                .toList();
    }

    /**
     * 현재 시간 기준으로 만료 처리시켜야 할 토큰들을 반환한다.
     */
    public List<WaitingQueueInfo> getWaitingTokensToExpire(LocalDateTime now) {
        return waitingQueueRepository.findByWaitingStatus().stream()
                .filter(queue -> queue.isExpiredTime(now))
                .map(WaitingQueueInfo::fromEntity)
                .toList();
    }

    /**
     * 현재 시간 기준으로 입장하여 예약 진행중인 토큰들을 반환한다.
     */
    public List<WaitingQueueInfo> getProcessingTokens(LocalDateTime now) {
        return waitingQueueRepository.findByWaitingStatus().stream()
                .filter(queue -> queue.isProcessing(now))
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
