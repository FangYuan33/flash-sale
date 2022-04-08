package com.actionworks.flashsale.mq.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 下单任务
 *
 * @author fangyuan
 */
@Data
public class PlaceOrderTask implements Serializable {
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
