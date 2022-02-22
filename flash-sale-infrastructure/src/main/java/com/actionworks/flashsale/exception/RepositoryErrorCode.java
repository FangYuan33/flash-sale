package com.actionworks.flashsale.exception;

import com.alibaba.cola.dto.ErrorCodeI;

/**
 * RepositoryException专用异常枚举
 */
public enum RepositoryErrorCode implements ErrorCodeI {

    /**
     * 通用异常枚举
     */
    ID_NOT_EXIST("500", "ID为空");

    private final String errorCode;

    private final String errorDesc;

    RepositoryErrorCode(String errorCode, String errorDesc) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    @Override
    public String getErrCode() {
        return errorCode;
    }

    @Override
    public String getErrDesc() {
        return errorDesc;
    }
}
