package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.dto.LoginRequest;
import com.gov.systemservice.dto.LoginResponse;
import com.gov.systemservice.dto.RegisterRequest;
import com.gov.systemservice.dto.ResetPasswordRequest;
import com.gov.systemservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户控制器
 * 处理用户登录、注册、密码重置等请求
 * 
 * @author chengbin
 * @since 2026-04-19
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     * @param request 登录请求
     * @param httpRequest HTTP请求
     * @return 登录响应
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        LoginResponse response = userService.login(request, ip);
        return Result.success(response);
    }

    /**
     * 用户注册
     * @param request 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody RegisterRequest request) {
        boolean result = userService.register(request);
        return Result.success(result);
    }

    /**
     * 密码重置
     * @param request 密码重置请求
     * @return 重置结果
     */
    @PostMapping("/reset-password")
    public Result<Boolean> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean result = userService.resetPassword(request);
        return Result.success(result);
    }

    /**
     * 获取客户端IP地址
     * @param request HTTP请求
     * @return 客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
