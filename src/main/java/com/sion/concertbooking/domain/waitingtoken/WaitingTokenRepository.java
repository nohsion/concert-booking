package com.sion.concertbooking.domain.waitingtoken;

import java.util.Optional;

public interface WaitingTokenRepository {

    WaitingToken save(WaitingToken waitingToken);

    Optional<WaitingToken> findByTokenId(String tokenId);
}
