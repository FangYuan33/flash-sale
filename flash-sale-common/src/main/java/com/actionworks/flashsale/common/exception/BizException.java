package com.actionworks.flashsale.common.exception;

/**
 * app层业务异常
 *
 * @author fangyuan
 */
public class BizException extends AppException {
    public BizException(AppErrorCode appErrorCode) {
        super(appErrorCode.getErrorDesc());
    }
}