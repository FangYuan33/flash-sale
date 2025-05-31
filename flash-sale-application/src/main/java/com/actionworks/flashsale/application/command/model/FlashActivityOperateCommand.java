package com.actionworks.flashsale.application.command.model;

import lombok.Data;

@Data
public class FlashActivityOperateCommand {

    /**
     * 活动code
     */
    private String code;

    /**
     * 活动状态
     */
    private Integer status;
}

