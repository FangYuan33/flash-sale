package com.actionworks.flashsale.domain.event;

import com.alibaba.cola.event.DomainEventI;

/**
 * 领域时间发布接口
 *
 * @author fangyuan
 */
public interface DomainEventPublisher {

    /**
     * 发布事件
     */
    void publish(DomainEventI domainEvent);
}
