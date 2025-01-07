package com.sion.concertbooking.domain.info;

import com.sion.concertbooking.domain.entity.PointHistory;
import com.sion.concertbooking.domain.enums.TransactionType;

public record PointHistoryInfo(
        long pointHistoryId,
        long pointId,
        int amount,
        TransactionType transactionType
) {
    public static PointHistoryInfo fromEntity(PointHistory pointHistory) {
        return new PointHistoryInfo(
                pointHistory.getId(),
                pointHistory.getPointId(),
                pointHistory.getAmount(),
                pointHistory.getType()
        );
    }
}
