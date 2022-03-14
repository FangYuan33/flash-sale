package com.actionworks.flashsale.domain.event.entity;

import com.actionworks.flashsale.domain.event.enums.ActivityEventType;

public class FlashActivityEvent extends BaseDomainEvent {

    private final ActivityEventType eventType;

    public FlashActivityEvent(Long id, ActivityEventType eventType) {
        this.id = id;
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "FlashActivityEvent{" + "id=" + id + ", eventType=" + eventType.getDesc() + '}';
    }
}
