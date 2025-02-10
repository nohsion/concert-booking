package com.sion.concertbooking.intefaces.presentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.application.waitingqueue.WaitingQueueResult;

import java.time.LocalDateTime;

public record WaitingQueueRegisterResponse(
        @JsonProperty(value = "userId") long userId,
        @JsonProperty(value = "concertId") long concertId,
        @JsonProperty(value = "tokenId") String tokenId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonProperty(value = "createdAt") LocalDateTime createdAt
) {

        public static WaitingQueueRegisterResponse fromResult(WaitingQueueResult result) {
                return new WaitingQueueRegisterResponse(
                        result.userId(),
                        result.concertId(),
                        result.tokenId(),
                        result.createdAt()
                );
        }
}
