package com.actionworks.flashsale.domain.event.entity;

import com.actionworks.flashsale.domain.event.enums.ItemEventType;

public class FlashItemEvent extends BaseDomainEvent {

    private ItemEventType eventType;

    public FlashItemEvent(Long id, ItemEventType eventType) {
        this.id = id;
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "FlashItemEvent{" + "id=" + id + ", eventType=" + eventType.getDesc() + '}';
    }
}
