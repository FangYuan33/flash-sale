package com.actionworks.flashsale.application.mq.impl;

import com.actionworks.flashsale.application.mq.PlaceOrderTaskPostService;
import com.actionworks.flashsale.infrastructure.mq.message.PlaceOrderTask;
import com.actionworks.flashsale.infrastructure.mq.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Slf4j
@Service
@ConditionalOnProperty(name = "placeOrder.type", havingValue = "queued")
public class KafkaOrderTaskService implements PlaceOrderTaskPostService {

    @Value("${placeOrder.topic}")
    private String flashSaleTopic;
    @Resource
    private KafkaService kafkaService;

    @Override
    public boolean post(PlaceOrderTask placeOrderTask) {
        return kafkaService.sendSync(flashSaleTopic, placeOrderTask);
    }
}
