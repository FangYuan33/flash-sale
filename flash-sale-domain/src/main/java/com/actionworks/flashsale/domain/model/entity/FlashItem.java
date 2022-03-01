package com.actionworks.flashsale.domain.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class FlashItem {

    /**
     * 秒杀品ID
     */
    private Long id;

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
     * 校验发布秒杀商品的参数
     * 商品标题、开始时间、结束时间非空
     * 结束时间在开始之间之后、结束时间大于当前时间
     */
    public boolean validateParamsForCreate() {
        return !StringUtils.isEmpty(itemTitle) && startTime != null && endTime != null
                && !endTime.isBefore(startTime) && !endTime.isBefore(LocalDateTime.now());
    }
}
