package com.actionworks.flashsale.domain.model.event;

import com.actionworks.flashsale.domain.event.BaseDomainEvent;

import java.time.LocalDateTime;

public class CreateOrderEvent extends BaseDomainEvent<String> {

    public CreateOrderEvent(String id, String messageBody, LocalDateTime bizTime) {
        super(id, messageBody, bizTime);
    }
}
