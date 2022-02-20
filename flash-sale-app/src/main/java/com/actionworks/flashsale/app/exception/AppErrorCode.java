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
    TRY_LATER("TRY_LATER", "稍后再试"),
    FREQUENTLY_ERROR("FREQUENTLY_ERROR", "操作频繁，稍后再试"),
    BUSINESS_ERROR("BUSINESS_ERROR", "未知错误");

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
