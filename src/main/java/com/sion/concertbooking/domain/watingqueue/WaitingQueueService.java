package com.sion.concertbooking.domain.watingqueue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class WaitingQueueService {

    private final WaitingQueueRepository waitingQueueRepository;

    public WaitingQueueService(
            WaitingQueueRepository waitingQueueRepository
    ) {
        this.waitingQueueRepository = waitingQueueRepository;
    }

    public String waitQueue(WaitingQueueIssueCommand command) {
        WaitingQueue waitingQueue = WaitingQueue.of(
                command.tokenId(), command.userId(), command.concertId(), command.now()
        );
        // 대기열 저장
        String savedTokenId = waitingQueueRepository.save(waitingQueue);
        // 대기 콘서트 추가
        waitingQueueRepository.addWaitingConcert(command.concertId());
        return savedTokenId;
    }

    public long getRank(String tokenId, long concertId) {
        Long rankOfQueue = waitingQueueRepository.findRank(tokenId, concertId);
        if (rankOfQueue == null) {
            throw new NoSuchElementException("해당 토큰은 대기열에 존재하지 않습니다. tokenId=" + tokenId);
        }
        return rankOfQueue;
    }

    public void popMinByConcertId(int count, long concertId) {
        waitingQueueRepository.popMin(count, concertId);
    }

    public List<String> getWaitingTokens(long concertId) {
        return waitingQueueRepository.getWaitingTokens(concertId);
    }

    public void removeToken(String tokenId, long concertId) {
        waitingQueueRepository.removeToken(tokenId, concertId);
    }

    public List<Long> getWaitingConcerts() {
        return waitingQueueRepository.getWaitingConcerts();
    }

    public void removeWaitingConcert(long concertId) {
        waitingQueueRepository.removeWaitingConcert(concertId);
    }
}
