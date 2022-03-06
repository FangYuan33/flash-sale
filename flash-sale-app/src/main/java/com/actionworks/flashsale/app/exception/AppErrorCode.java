package com.actionworks.flashsale.app.exception;

import com.alibaba.cola.dto.ErrorCodeI;

/**
 * service异常枚举
 *
 * @author fangyuan
 */
public enum AppErrorCode implements ErrorCodeI {

    /**
     * 一般性错误
     */
    INVALID_PARAMS("INVALID_PARAMS", "参数错误"),

    ACTIVITY_NOT_EXIST("ACTIVITY_NOT_EXIST", "秒杀活动不存在"),
    DO_PLACE_ORDER("DO_PLACE_ORDER", "秒杀失败");

    /**
     * 错误码
     */
    private final String errCode;

    /**
     * 错误信息
     */
    private final String errorDesc;

    AppErrorCode(String errCode, String errorDesc) {
        this.errCode = errCode;
        this.errorDesc = errorDesc;
    }

    @Override
    public String getErrCode() {
        return errCode;
    }

    @Override
    public String getErrDesc() {
        return errorDesc;
    }
}
