package com.actionworks.flashsale.common.exception;

/**
 * app层专用Exception
 *
 * @author fangyuan
 */
public class AppException extends RuntimeException {

    public AppException(String errorMsg) {
        super(errorMsg);
    }
}
