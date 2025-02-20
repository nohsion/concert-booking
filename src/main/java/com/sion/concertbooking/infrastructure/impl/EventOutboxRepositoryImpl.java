package com.sion.concertbooking.infrastructure.impl;

import com.sion.concertbooking.domain.event.EventOutbox;
import com.sion.concertbooking.domain.event.EventOutboxRepository;
import com.sion.concertbooking.infrastructure.jpa.EventOutboxJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class EventOutboxRepositoryImpl implements EventOutboxRepository {

    private final EventOutboxJpaRepository eventOutboxJpaRepository;

    public EventOutboxRepositoryImpl(EventOutboxJpaRepository eventOutboxJpaRepository) {
        this.eventOutboxJpaRepository = eventOutboxJpaRepository;
    }

    @Override
    public Optional<EventOutbox> findById(final long eventId) {
        return eventOutboxJpaRepository.findById(eventId);
    }

    @Override
    public EventOutbox save(EventOutbox eventOutbox) {
        return eventOutboxJpaRepository.save(eventOutbox);
    }

    @Override
    public EventOutbox findByStatus(EventOutbox.Status status) {
        return eventOutboxJpaRepository.findByStatus(status);
    }

    @Override
    public EventOutbox findByAggregateTypeAndAggregateIdAndEventType(
            String aggregateType, long aggregateId, String eventType
    ) {
        return eventOutboxJpaRepository.findByAggregateTypeAndAggregateIdAndEventType(
                aggregateType, aggregateId, eventType);
    }
}
