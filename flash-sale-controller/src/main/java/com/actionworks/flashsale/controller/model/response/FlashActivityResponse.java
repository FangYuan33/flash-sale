package com.actionworks.flashsale.controller.model.response;

import java.time.LocalDateTime;

/**
 * controller层返回的对象类型
 */
public class FlashActivityResponse {
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
