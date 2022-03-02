package com.actionworks.flashsale.app.model.query;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlashItemQuery {
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
}
