package com.actionworks.flashsale.application.mq.listener;

import com.actionworks.flashsale.application.model.command.FlashPlaceOrderCommand;
import com.actionworks.flashsale.application.service.placeOrder.DoPlaceOrderService;
import com.actionworks.flashsale.infrastructure.mq.message.PlaceOrderTask;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@ConditionalOnProperty(name = "placeOrder.type", havingValue = "queued")
public class KafkaConsumerListener {

    @Resource
    private DoPlaceOrderService placeOrderService;

    @KafkaListener(topics = "${placeOrder.topic}", groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    private void processOrderTask(PlaceOrderTask message) {
        FlashPlaceOrderCommand placeOrderCommand = new FlashPlaceOrderCommand();
        BeanUtils.copyProperties(message, placeOrderCommand);

        placeOrderService.doPlaceOrder(message.getUserId(), placeOrderCommand);
    }
}
