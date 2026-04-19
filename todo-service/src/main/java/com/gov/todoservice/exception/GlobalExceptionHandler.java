package com.gov.todoservice.exception;

import com.gov.common.Result;
import com.gov.common.utils.LogUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        logException(e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(IllegalArgumentException e) {
        logException(e);
        return Result.badRequest(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        logException(e);
        return Result.error("Internal server error: " + e.getMessage());
    }

    /**
     * 记录异常日志
     * @param e 异常
     */
    private void logException(Exception e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestUrl = request.getRequestURI();
        String clientIp = request.getRemoteAddr();
        String method = request.getMethod();

        LogUtils.error(GlobalExceptionHandler.class, 
            "[异常处理] 请求URL: {} 方法: {} IP: {} 异常: {}", 
            requestUrl, method, clientIp, e.getMessage(), e);
    }
}
