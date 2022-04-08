package com.actionworks.flashsale.app.mq.impl;

import com.actionworks.flashsale.app.mq.PlaceOrderTaskPostService;
import com.actionworks.flashsale.app.mq.model.PlaceOrderTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@ConditionalOnProperty(name = "placeOrder.type", havingValue = "queued")
public class KafkaOrderTaskService implements PlaceOrderTaskPostService {

    @Override
    public boolean post(PlaceOrderTask placeOrderTask) {
        return false;
    }
}
