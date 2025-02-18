package com.sion.concertbooking.domain.event;

public interface EventOutboxRepository {
    EventOutbox save(EventOutbox eventOutbox);
}
