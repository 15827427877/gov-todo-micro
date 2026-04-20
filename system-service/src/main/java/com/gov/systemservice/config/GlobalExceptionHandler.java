package com.gov.systemservice.config;

import com.gov.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 用于捕获权限校验失败时抛出的异常，并返回统一的错误响应
 *
 * @author chengbin
 * @since 2026-04-20
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理权限校验失败异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        return Result.error(e.getMessage());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        return Result.error("系统内部错误: " + e.getMessage());
    }
}