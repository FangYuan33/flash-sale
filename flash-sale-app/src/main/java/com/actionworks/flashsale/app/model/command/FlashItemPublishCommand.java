package com.actionworks.flashsale.app.model.command;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlashItemPublishCommand {
    /**
     * 秒杀品名称标题
     */
    private String itemTitle;

    /**
     * 秒杀品副标题
     */
    private String itemSubTitle;

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
     * 秒杀开始时间
     */
    private LocalDateTime startTime;

    /**
     * 秒杀结束时间
     */
    private LocalDateTime endTime;
}
