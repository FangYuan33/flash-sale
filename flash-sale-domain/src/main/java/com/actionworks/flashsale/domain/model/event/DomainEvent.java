package com.actionworks.flashsale.domain.model.event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 领域事件抽象基类
 * 所有领域事件都应该继承此类
 */
@Getter
public abstract class DomainEvent {
    
    /**
     * 事件ID
     */
    private final String eventId;
    
    /**
     * 事件类型
     */
    private final String eventType;
    
    /**
     * 事件发生时间
     */
    private final LocalDateTime occurredOn;
    
    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = this.getClass().getSimpleName();
        this.occurredOn = LocalDateTime.now();
    }

} 