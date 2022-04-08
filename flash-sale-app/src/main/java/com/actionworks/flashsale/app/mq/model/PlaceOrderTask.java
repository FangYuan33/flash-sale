package com.actionworks.flashsale.app.mq.model;

/**
 * 下单任务
 *
 * @author fangyuan
 */
public class PlaceOrderTask {
    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 下单数量
     */
    private Integer quantity;
}
