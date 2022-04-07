package com.actionworks.flashsale.mq.impl;

import com.actionworks.flashsale.mq.PlaceOrderTaskPostService;
import com.actionworks.flashsale.mq.model.PlaceOrderTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
@ConditionalOnProperty(name = "placeOrder.type", havingValue = "queued")
public class KafkaOrderTaskService implements PlaceOrderTaskPostService {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public boolean post(PlaceOrderTask placeOrderTask) {
        return false;
    }
}
