package com.actionworks.flashsale.application.query.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlashActivityDTO {

    /**
     * 活动ID
     */
    private Long id;

    private String code;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 秒杀品
     */
    private FlashItemDTO flashItem;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    private Integer status;

}
