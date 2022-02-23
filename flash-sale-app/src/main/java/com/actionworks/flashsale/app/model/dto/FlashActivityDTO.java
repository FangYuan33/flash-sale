package com.actionworks.flashsale.app.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务层DTO传参对象
 */
@Data
public class FlashActivityDTO {

    /**
     * 活动ID
     */
    private Long id;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 活动开始时间
     */
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime endTime;

    /**
     * 活动状态
     */
    private Integer status;
}
