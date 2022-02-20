package com.actionworks.flashsale.domain.exception;


import com.alibaba.cola.dto.ErrorCodeI;

/**
 * Domain层专用异常枚举
 *
 * @author fangyuan
 */
public enum DomainErrorCode implements ErrorCodeI {

    /**
     * 通用错误码
     */
    PARAMS_INVALID("PARAMS_INVALID", "参数错误");

    /**
     * 错误码
     */
    private final String errCode;

    /**
     * 错误消息
     */
    private final String errDesc;

    DomainErrorCode(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    @Override
    public String getErrCode() {
        return errCode;
    }

    @Override
    public String getErrDesc() {
        return errDesc;
    }
}