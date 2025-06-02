package com.actionworks.flashsale.application.command.model;

import lombok.Data;

@Data
public class FlashOrderCreateCommand {

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
