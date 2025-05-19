package com.actionworks.flashsale.application.exception;

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
