package com.actionworks.flashsale.domain.event;

public interface DomainEventPublisher {

    <T> void publishEvent(BaseDomainEvent<T> event);

}
