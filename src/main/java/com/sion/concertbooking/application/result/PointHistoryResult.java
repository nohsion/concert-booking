package com.sion.concertbooking.application.result;

import com.sion.concertbooking.domain.enums.TransactionType;
import com.sion.concertbooking.domain.info.PointHistoryInfo;

import java.time.LocalDateTime;

public record PointHistoryResult(
        long pointHistoryId,
        long pointId,
        int amount,
        TransactionType transactionType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PointHistoryResult fromInfo(PointHistoryInfo info) {
        return new PointHistoryResult(
                info.pointHistoryId(),
                info.pointId(),
                info.amount(),
                info.transactionType(),
                info.createdAt(),
                info.updatedAt()
        );
    }
}
