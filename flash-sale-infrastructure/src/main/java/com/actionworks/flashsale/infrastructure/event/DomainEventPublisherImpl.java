package com.actionworks.flashsale.infrastructure.event;

import com.actionworks.flashsale.domain.event.BaseDomainEvent;
import com.actionworks.flashsale.domain.event.DomainEventPublisher;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DomainEventPublisherImpl implements DomainEventPublisher {

    @Override
    public <T> void publishEvent(BaseDomainEvent<T> event) {
        log.info("发送消息：{}", JSONObject.toJSONString(event));
    }

}
