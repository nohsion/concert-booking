package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.domain.watingqueue.WaitingQueueInfo;

import java.time.LocalDateTime;

public record WaitingQueueRegisterResponse(
        @JsonProperty(value = "userId") long userId,
        @JsonProperty(value = "concertId") long concertId,
        @JsonProperty(value = "tokenId") String tokenId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonProperty(value = "createdAt") LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonProperty(value = "expiredAt") LocalDateTime expiredAt
) {

        public static WaitingQueueRegisterResponse fromDto(WaitingQueueInfo dto) {
                return new WaitingQueueRegisterResponse(
                        dto.userId(),
                        dto.concertId(),
                        dto.tokenId(),
                        dto.createdAt(),
                        dto.expiredAt()
                );
        }
}
