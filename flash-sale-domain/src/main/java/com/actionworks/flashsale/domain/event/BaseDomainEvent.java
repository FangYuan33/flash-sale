package com.actionworks.flashsale.domain.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public abstract class BaseDomainEvent<T> {

    private String id;

    private T messageBody;

    /**
     * 业务发生时间
     */
    private LocalDateTime bizTime;

    public BaseDomainEvent(String id, T messageBody, LocalDateTime bizTime) {
        this.id = id;
        this.messageBody = messageBody;
        this.bizTime = bizTime;
    }
}
