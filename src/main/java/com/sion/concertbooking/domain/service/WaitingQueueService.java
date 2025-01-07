package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.model.info.WaitingQueueInfo;
import com.sion.concertbooking.domain.dto.WaitingQueueDetailInfo;
import com.sion.concertbooking.domain.model.entity.WaitingQueue;
import com.sion.concertbooking.domain.enums.WaitingQueueStatus;
import com.sion.concertbooking.domain.repository.WaitingQueueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public WaitingQueueInfo waitQueueAndIssueToken(long userId, long concertId, LocalDateTime now) {
        String tokenId = tokenProvider.generateToken();
        WaitingQueue waitingQueue = WaitingQueue.of(tokenId, userId, concertId, now);
        WaitingQueue savedEntity = waitingQueueRepository.save(waitingQueue);

        return WaitingQueueInfo.fromEntity(savedEntity);
    }

    public WaitingQueueInfo getQueueByTokenId(String tokenId) {
        WaitingQueue waitingQueue = waitingQueueRepository.findByTokenId(tokenId);
        if (waitingQueue == null) {
            throw new IllegalArgumentException("Invalid token");
        }
        return WaitingQueueInfo.fromEntity(waitingQueue);
    }

    public WaitingQueueDetailInfo getQueueInfoByTokenId(String tokenId) {
        WaitingQueue waitingQueue = waitingQueueRepository.findByTokenId(tokenId);
        if (waitingQueue == null) {
            throw new IllegalArgumentException("Invalid token");
        }
        // 입장한 경우
        if (WaitingQueueStatus.ENTERED == waitingQueue.getStatus()) {
            return new WaitingQueueDetailInfo(waitingQueue.getTokenId(), 0, 0);
        }
        // 대기중인 경우
        List<WaitingQueue> waitingQueues = waitingQueueRepository.findWaitingQueue(LocalDateTime.now());
        int remainingWaitingOrder = waitingQueues.indexOf(waitingQueue) + 1;
        int remainingTimeSec = 10; // TODO: 남은 대기 시간 계산.. 10초로 임시 설정

        return new WaitingQueueDetailInfo(waitingQueue.getTokenId(), remainingWaitingOrder, remainingTimeSec);
    }

    public boolean isTokenValid(String tokenId, LocalDateTime now) {
        WaitingQueue waitingQueue = waitingQueueRepository.findByTokenId(tokenId);
        if (waitingQueue == null) {
            return false;
        }
        return !waitingQueue.isExpired(now);
    }
}
