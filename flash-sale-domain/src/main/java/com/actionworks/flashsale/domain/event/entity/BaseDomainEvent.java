package com.actionworks.flashsale.domain.event.entity;

import com.alibaba.cola.event.DomainEventI;
import lombok.Getter;

@Getter
public class BaseDomainEvent implements DomainEventI {

    /**
     * id值
     */
    protected Long id;
}
