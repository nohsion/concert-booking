package com.sion.concertbooking.domain.waitingtoken;

import com.sion.concertbooking.domain.watingqueue.WaitingQueueIssueCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
public class WaitingTokenService {

    private final WaitingTokenRepository waitingTokenRepository;

    public WaitingTokenService(
            WaitingTokenRepository waitingTokenRepository
    ) {
        this.waitingTokenRepository = waitingTokenRepository;
    }

    public WaitingTokenInfo issue(WaitingQueueIssueCommand command) {
        WaitingToken waitingToken = WaitingToken.of(
                command.tokenId(), command.userId(), command.concertId(), command.now()
        );
        WaitingToken savedEntity = waitingTokenRepository.save(waitingToken);

        return WaitingTokenInfo.fromEntity(savedEntity);
    }

    public WaitingTokenInfo getToken(String tokenId) {
        WaitingToken waitingToken = waitingTokenRepository.findByTokenId(tokenId)
                .orElseThrow(() -> new NoSuchElementException("tokenId=" + tokenId + "에 해당하는 토큰이 없습니다."));
        return WaitingTokenInfo.fromEntity(waitingToken);
    }

}
