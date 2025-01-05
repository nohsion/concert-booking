package com.sion.concertbooking.domain.service;

import com.sion.concertbooking.domain.dto.WaitingQueueDto;
import com.sion.concertbooking.domain.entity.WaitingQueue;
import com.sion.concertbooking.domain.repository.WaitingQueueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}
