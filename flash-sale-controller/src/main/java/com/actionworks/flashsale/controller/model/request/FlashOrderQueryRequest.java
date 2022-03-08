package com.actionworks.flashsale.controller.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FlashOrderQueryRequest extends BaseQueryRequest {
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
}
