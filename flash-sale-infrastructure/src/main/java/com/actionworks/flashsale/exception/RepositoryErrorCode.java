package com.actionworks.flashsale.exception;

/**
 * RepositoryException专用异常枚举
 */
public enum RepositoryErrorCode {

    /**
     * 通用异常枚举
     */
    ID_NOT_EXIST("ID为空");

    private final String errorDesc;

    RepositoryErrorCode(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getErrorDesc() {
        return errorDesc;
    }
}
