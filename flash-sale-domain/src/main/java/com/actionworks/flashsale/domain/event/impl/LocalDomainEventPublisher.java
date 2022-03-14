package com.actionworks.flashsale.domain.event.impl;

import com.actionworks.flashsale.domain.event.DomainEventPublisher;
import com.alibaba.cola.event.DomainEventI;
import com.alibaba.cola.event.EventBusI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 本地领域事件发布器
 */
@Slf4j
@Component
public class LocalDomainEventPublisher implements DomainEventPublisher {

    @Resource
    private EventBusI eventBus;

    @Override
    public void publish(DomainEventI domainEvent) {
        log.info("发布事件, {}", domainEvent.toString());

        eventBus.fire(domainEvent);
    }
}
