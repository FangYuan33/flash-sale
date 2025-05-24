package com.actionworks.flashsale.trigger.exception;

import com.actionworks.flashsale.common.exception.BizException;
import com.actionworks.flashsale.common.exception.DomainException;
import com.actionworks.flashsale.trigger.model.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(value = {BizException.class, DomainException.class})
    public Response businessException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return Response.buildFailure(e.getMessage());
    }

    @ExceptionHandler(value = {SQLException.class, NullPointerException.class, Exception.class})
    public Response sqlExceptionAndNPE(Exception e) {
        log.error(e.getMessage(), e);

        if (e instanceof SQLException) {
            return Response.buildFailure("数据库操作异常");
        }

        if (e instanceof NullPointerException) {
            return Response.buildFailure("空指针异常");
        }

        return Response.buildFailure(e.getMessage());
    }
}
