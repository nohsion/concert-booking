package com.sion.concertbooking.domain.event;

import org.springframework.stereotype.Component;

@Component
public class EventOutboxRecorder {

    private final EventOutboxRepository eventOutboxRepository;

    public EventOutboxRecorder(EventOutboxRepository eventOutboxRepository) {
        this.eventOutboxRepository = eventOutboxRepository;
    }

    public EventOutbox save(EventOutbox eventOutbox) {
        return eventOutboxRepository.save(eventOutbox);
    }
}
