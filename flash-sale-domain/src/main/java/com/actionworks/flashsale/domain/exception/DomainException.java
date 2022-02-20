package com.actionworks.flashsale.domain.exception;

/**
 * Domain层专用异常
 */
public class DomainException extends RuntimeException {
    public DomainException(DomainErrorCode domainErrorCode) {
        super(domainErrorCode.getErrDesc());
    }
}