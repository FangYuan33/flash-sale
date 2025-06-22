package com.actionworks.flashsale.domain.model.event;

import com.actionworks.flashsale.domain.model.aggregate.FlashOrder;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

/**
 * 订单创建事件
 */
@Getter
public class OrderCreateEvent extends DomainEvent {

    // 以下是两种不同的发送消息的架构设计，这两种方式需要根据团队的实际情况和需求来选择

    // 这种只传 ID，消息的消费方需要通过 ID 查询订单信息在处理，这样做的好处是消息体小，而且数据总是最新的
    private final String orderCode;

    // 这种直接传订单的 JSON 数据，消息的消费方可以直接使用，减少了查询操作，但数据可能不是最新的
    private final String jsonData;

    public OrderCreateEvent(FlashOrder flashOrder) {
        this.orderCode = flashOrder.getCode();
        this.jsonData = JSONObject.toJSONString(flashOrder);
    }

} 