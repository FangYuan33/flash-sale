package com.actionworks.flashsale.exception;

/**
 * infrastructure 专用Exception
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(RepositoryErrorCode errorCode) {
        super(errorCode.getErrorDesc());
    }
}
