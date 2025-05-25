package com.actionworks.flashsale.common.exception;

/**
 * Domain层专用异常
 */
public class DomainException extends RuntimeException {
    public DomainException(DomainErrorCode domainErrorCode) {
        super(domainErrorCode.getErrDesc());
    }

    public DomainException(String message) {
        super(message);
    }
}