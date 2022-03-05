package com.actionworks.flashsale.app.model.command;

import lombok.Data;

@Data
public class FlashPlaceOrderCommand {
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
