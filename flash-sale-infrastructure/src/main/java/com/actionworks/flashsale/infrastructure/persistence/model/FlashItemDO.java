package com.actionworks.flashsale.infrastructure.persistence.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("flash_item")
@EqualsAndHashCode(callSuper = false)
public class FlashItemDO extends BaseDO {
    /**
     * 秒杀活动ID
     */
    private Long activityId;

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

    /**
     * 秒杀商品状态 10-已发布 20-已上线 30-已下线
     */
    private Integer status;

    /**
     * 库存预热情况 0-未预热 1-已预热
     */
    private Integer warmUp;
}
