package com.actionworks.flashsale.mq.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerListener {

    @KafkaListener
    public void listener() {

    }
}
