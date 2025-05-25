package com.actionworks.flashsale.application.command.model;

import lombok.Data;

@Data
public class FlashItemOperateCommand {

    /**
     * 编码
     */
    private String code;

    /**
     * 状态
     */
    private Integer status;

}
