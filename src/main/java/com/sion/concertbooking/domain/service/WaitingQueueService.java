package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.dto.WaitingQueueDto;
import com.sion.concertbooking.domain.dto.WaitingQueueInfo;
import com.sion.concertbooking.domain.entity.WaitingQueue;
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

    public WaitingQueueDto waitQueueAndIssueToken(long userId, long concertId, LocalDateTime now) {
        String tokenId = tokenProvider.generateToken();
        WaitingQueue waitingQueue = WaitingQueue.of(tokenId, userId, concertId, now);
        WaitingQueue savedEntity = waitingQueueRepository.save(waitingQueue);

        return WaitingQueueDto.fromEntity(savedEntity);
    }

    public WaitingQueueDto getQueueByTokenId(String tokenId) {
        WaitingQueue waitingQueue = waitingQueueRepository.findByTokenId(tokenId);
        if (waitingQueue == null) {
            throw new IllegalArgumentException("Invalid token");
        }
        return WaitingQueueDto.fromEntity(waitingQueue);
    }

    public WaitingQueueInfo getQueueInfoByTokenId(String tokenId) {
        WaitingQueue waitingQueue = waitingQueueRepository.findByTokenId(tokenId);
        if (waitingQueue == null) {
            throw new IllegalArgumentException("Invalid token");
        }
        // 입장한 경우
        if (WaitingQueueStatus.ENTERED == waitingQueue.getStatus()) {
            return new WaitingQueueInfo(waitingQueue.getTokenId(), 0, 0);
        }
        // 대기중인 경우
        List<WaitingQueue> waitingQueues = waitingQueueRepository.findWaitingQueue(LocalDateTime.now());
        int remainingWaitingOrder = waitingQueues.indexOf(waitingQueue) + 1;
        int remainingTimeSec = 10; // TODO: 남은 대기 시간 계산.. 10초로 임시 설정

        return new WaitingQueueInfo(waitingQueue.getTokenId(), remainingWaitingOrder, remainingTimeSec);
    }

    public boolean isTokenValid(String tokenId, LocalDateTime now) {
        WaitingQueue waitingQueue = waitingQueueRepository.findByTokenId(tokenId);
        if (waitingQueue == null) {
            return false;
        }
        if (waitingQueue.getExpiredAt().isBefore(now)) {
            return false;
        }
        return true;
    }
}
