package com.actionworks.flashsale.trigger.model.request;

import lombok.Data;

@Data
public class FlashItemOperateRequest {

    /**
     * 商品唯一编码
     */
    private String code;

    private Integer status;

}
