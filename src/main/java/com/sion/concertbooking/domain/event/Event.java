package com.sion.concertbooking.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;

@Getter
public abstract class Event {

    @JsonIgnore
    protected final String aggregateType;
    @JsonIgnore
    protected final String eventType;
    @JsonIgnore
    protected final long aggregateId;

    protected Event(String aggregateType, String eventType, long aggregateId) {
        this.aggregateType = aggregateType;
        this.eventType = eventType;
        this.aggregateId = aggregateId;
    }

    /**
     * 카프카에 저장할 json payload로 변환한다.
     */
    public String toPayload() {
        try {
            return EventOutboxUtils.OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize Event object", e);
        }
    }

    public EventOutbox toEventOutbox() {
        return EventOutbox.of(
                this.aggregateType,
                this.aggregateId,
                this.eventType,
                this.toPayload()
        );
    }
}
