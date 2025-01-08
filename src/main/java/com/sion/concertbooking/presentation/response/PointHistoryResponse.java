package com.sion.concertbooking.presentation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.application.result.PointHistoryResult;
import com.sion.concertbooking.domain.enums.TransactionType;

import java.time.LocalDateTime;

public record PointHistoryResponse(
        @JsonProperty("pointHistoryId") long pointHistoryId,
        @JsonProperty("pointId") long pointId,
        @JsonProperty("amount") int amount,
        @JsonProperty("transactionType") TransactionType transactionType,
        @JsonProperty("createdAt") LocalDateTime createdAt,
        @JsonProperty("updatedAt") LocalDateTime updatedAt
) {
    public static PointHistoryResponse fromResult(PointHistoryResult result) {
        return new PointHistoryResponse(
                result.pointHistoryId(),
                result.pointId(),
                result.amount(),
                result.transactionType(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}
