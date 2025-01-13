package com.sion.concertbooking.application.criteria;

import com.sion.concertbooking.domain.pointhistory.TransactionType;
import jakarta.annotation.Nullable;

public record PointHistoryCriteria(
        long userId,
        @Nullable TransactionType transactionType
) {
}
