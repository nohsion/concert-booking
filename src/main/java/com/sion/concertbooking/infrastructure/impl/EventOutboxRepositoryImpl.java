package com.sion.concertbooking.infrastructure.impl;

import com.sion.concertbooking.domain.event.EventOutbox;
import com.sion.concertbooking.domain.event.EventOutboxRepository;
import com.sion.concertbooking.infrastructure.jpa.EventOutboxJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class EventOutboxRepositoryImpl implements EventOutboxRepository {

    private final EventOutboxJpaRepository eventOutboxJpaRepository;

    public EventOutboxRepositoryImpl(EventOutboxJpaRepository eventOutboxJpaRepository) {
        this.eventOutboxJpaRepository = eventOutboxJpaRepository;
    }

    @Override
    public EventOutbox save(EventOutbox eventOutbox) {
        return eventOutboxJpaRepository.save(eventOutbox);
    }
}
