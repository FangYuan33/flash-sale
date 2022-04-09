package com.actionworks.flashsale.mq.service;

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
public class KafkaService {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 同步发送消息
     *
     * @param topic   主题
     * @param message 消息
     */
    public boolean sendSync(String topic, Object message) {
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topic, message);

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
