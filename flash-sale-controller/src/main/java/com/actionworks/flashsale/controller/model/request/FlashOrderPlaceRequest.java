package com.actionworks.flashsale.controller.model.request;

import lombok.Data;

@Data
public class FlashOrderPlaceRequest {

    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 下单数量
     */
    private Integer quantity;
}
