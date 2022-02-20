package com.actionworks.flashsale.app.model.command;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlashActivityPublishCommand {

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
