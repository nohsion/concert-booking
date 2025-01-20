package com.sion.concertbooking.application.pointhistory;

import com.sion.concertbooking.domain.pointhistory.PointHistory;
import jakarta.annotation.Nullable;

public record PointHistoryCriteria(
        long userId,
        @Nullable PointHistory.TransactionType transactionType
) {
}
