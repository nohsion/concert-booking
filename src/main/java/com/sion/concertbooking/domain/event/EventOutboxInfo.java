package com.sion.concertbooking.domain.event;

import java.time.LocalDateTime;

public record EventOutboxInfo(
        long eventId,
        String aggregateType,
        long aggregateId,
        String eventType,
        String payload,
        EventOutbox.Status status,
        LocalDateTime createdAt
) {

    public static EventOutboxInfo fromEntity(EventOutbox eventOutbox) {
        return new EventOutboxInfo(
                eventOutbox.getEventId(),
                eventOutbox.getAggregateType(),
                eventOutbox.getAggregateId(),
                eventOutbox.getEventType(),
                eventOutbox.getPayload(),
                eventOutbox.getStatus(),
                eventOutbox.getCreatedAt()
        );
    }
}
