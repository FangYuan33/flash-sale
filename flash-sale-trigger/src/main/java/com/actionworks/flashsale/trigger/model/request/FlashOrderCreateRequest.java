package com.actionworks.flashsale.trigger.model.request;

import lombok.Data;

@Data
public class FlashOrderCreateRequest {

    /**
     * 商品唯一编码
     */
    private String itemCode;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 数量
     */
    private Integer quantity;

}
