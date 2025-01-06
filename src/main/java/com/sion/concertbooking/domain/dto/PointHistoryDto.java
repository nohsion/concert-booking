package com.sion.concertbooking.domain.dto;

import com.sion.concertbooking.domain.entity.PointHistory;
import com.sion.concertbooking.domain.enums.TransactionType;

public record PointHistoryDto(
        long pointHistoryId,
        long pointId,
        int amount,
        TransactionType transactionType
) {
    public static PointHistoryDto fromEntity(PointHistory pointHistory) {
        return new PointHistoryDto(
                pointHistory.getId(),
                pointHistory.getPointId(),
                pointHistory.getAmount(),
                pointHistory.getType()
        );
    }
}
