package com.actionworks.flashsale.common.exception;

/**
 * service异常枚举
 *
 * @author fangyuan
 */
public enum AppErrorCode {

    /**
     * 一般性错误
     */
    INVALID_PARAMS("参数错误"),

    ACTIVITY_NOT_EXIST("秒杀活动不存在"),
    DO_PLACE_ORDER("秒杀失败");

    /**
     * 错误信息
     */
    private final String errorDesc;

    AppErrorCode(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getErrorDesc() {
        return errorDesc;
    }
}
