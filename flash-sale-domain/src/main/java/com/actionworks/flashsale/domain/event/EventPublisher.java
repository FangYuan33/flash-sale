package com.actionworks.flashsale.domain.event;

import com.actionworks.flashsale.domain.model.event.DomainEvent;

/**
 * 事件发布器接口
 * 用于发布领域事件
 */
public interface EventPublisher {
    
    /**
     * 发布领域事件
     * 
     * @param event 领域事件
     */
    void publish(DomainEvent event);
} 