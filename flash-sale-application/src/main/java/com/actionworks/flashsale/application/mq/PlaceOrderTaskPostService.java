package com.actionworks.flashsale.application.mq;

import com.actionworks.flashsale.infrastructure.mq.message.PlaceOrderTask;

/**
 * 秒杀下单任务发布服务
 *
 * @author fangyuan
 */
public interface PlaceOrderTaskPostService {

    /**
     * 发布下单任务
     */
    boolean post(PlaceOrderTask placeOrderTask);
}
