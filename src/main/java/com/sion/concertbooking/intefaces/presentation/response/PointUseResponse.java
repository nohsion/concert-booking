package com.sion.concertbooking.intefaces.presentation.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sion.concertbooking.application.point.PointResult;
import com.sion.concertbooking.domain.pointhistory.PointHistory;

import java.time.LocalDateTime;

public record PointUseResponse(
        @JsonProperty(value = "userId") Long userId,
        @JsonProperty(value = "amount") int amount,
        @JsonProperty(value = "balance") int balance,
        @JsonProperty(value = "transactionType") PointHistory.TransactionType transactionType,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonProperty(value = "updatedAt") LocalDateTime updatedAt
) {
        public static PointUseResponse fromResult(PointResult.Use result) {
                return new PointUseResponse(
                        result.getUserId(),
                        result.getAmount(),
                        result.getBalance(),
                        result.getTransactionType(),
                        result.getUpdatedAt()
                );
        }
}
