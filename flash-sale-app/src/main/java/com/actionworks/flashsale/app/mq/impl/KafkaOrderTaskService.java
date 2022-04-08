package com.actionworks.flashsale.app.mq.impl;

import com.actionworks.flashsale.app.mq.PlaceOrderTaskPostService;
import com.actionworks.flashsale.mq.message.PlaceOrderTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
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
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>("flash-topic", placeOrderTask);

        SendResult<String, Object> result;
        try {
            result = kafkaTemplate.send(producerRecord).get();
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return false;
        }

        return result != null;
    }
}
