package com.actionworks.flashsale.common.exception;

/**
 * infrastructure 专用Exception
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(RepositoryErrorCode errorCode) {
        super(errorCode.getErrorDesc());
    }
}
