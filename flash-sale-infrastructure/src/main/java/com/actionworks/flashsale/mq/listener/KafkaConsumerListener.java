package com.actionworks.flashsale.mq.listener;

import com.actionworks.flashsale.mq.message.PlaceOrderTask;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerListener {

    @KafkaListener(topics = "${placeOrder.topic}", groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    private void processOrderTask(PlaceOrderTask message) {
        System.out.println(message);
    }
}
