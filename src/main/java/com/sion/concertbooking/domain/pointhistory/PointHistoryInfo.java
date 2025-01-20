package com.sion.concertbooking.domain.pointhistory;

import java.time.LocalDateTime;

public record PointHistoryInfo(
        long pointHistoryId,
        long pointId,
        int amount,
        PointHistory.TransactionType transactionType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PointHistoryInfo fromEntity(PointHistory pointHistory) {
        return new PointHistoryInfo(
                pointHistory.getId(),
                pointHistory.getPointId(),
                pointHistory.getAmount(),
                pointHistory.getType(),
                pointHistory.getCreatedAt(),
                pointHistory.getUpdatedAt()
        );
    }
}
