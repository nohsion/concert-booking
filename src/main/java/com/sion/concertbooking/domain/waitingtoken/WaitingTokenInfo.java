package com.sion.concertbooking.domain.waitingtoken;

import java.time.LocalDateTime;

public record WaitingTokenInfo(
        String tokenId,
        long userId,
        long concertId,
        LocalDateTime createdAt
) {
    public static WaitingTokenInfo fromEntity(WaitingToken entity) {
        return new WaitingTokenInfo(
                entity.getTokenId(),
                entity.getUserId(),
                entity.getConcertId(),
                entity.getCreatedAt()
        );
    }
}
