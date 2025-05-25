package com.actionworks.flashsale.application.query.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 秒杀商品app层出参类型
 */
@Data
public class FlashItemDTO {

    /**
     * 秒杀品ID
     */
    private Long id;

    /**
     * 品编码
     */
    private String code;

    /**
     * 秒杀品名称标题
     */
    private String itemTitle;

    /**
     * 秒杀品介绍
     */
    private String itemDesc;

    /**
     * 秒杀品初始库存
     */
    private Integer initialStock;

    /**
     * 秒杀品可用库存
     */
    private Integer availableStock;

    /**
     * 秒杀品原价
     */
    private Long originalPrice;

    /**
     * 秒杀价
     */
    private Long flashPrice;

    /**
     * 秒杀商品状态 10-已发布 20-已上线 30-已下线
     */
    private Integer status;
}
