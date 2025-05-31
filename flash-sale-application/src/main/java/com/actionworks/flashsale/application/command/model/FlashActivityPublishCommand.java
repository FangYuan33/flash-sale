package com.actionworks.flashsale.application.command.model;

import lombok.Data;

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
     * 秒杀品 code
     */
    private String flashItemCode;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}

