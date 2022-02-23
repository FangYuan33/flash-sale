package com.actionworks.flashsale.app.model.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 秒杀活动查询条件
 */
@Data
public class FlashActivitiesQuery {
    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 状态条件
     */
    private Integer status;

    /**
     * 开始时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 查询条件 每页展示的个数
     */
    private Integer pageSize;

    /**
     * 查询条件 展示的页面数
     */
    private Integer pageNum;
}
