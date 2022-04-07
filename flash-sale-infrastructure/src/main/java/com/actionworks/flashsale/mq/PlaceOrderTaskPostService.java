package com.actionworks.flashsale.mq;

import com.actionworks.flashsale.mq.model.PlaceOrderTask;

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
