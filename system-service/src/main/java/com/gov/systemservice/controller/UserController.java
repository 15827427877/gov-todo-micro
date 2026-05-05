package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.annotation.RequiresPermission;
import com.gov.systemservice.dto.LoginRequest;
import com.gov.systemservice.dto.LoginResponse;
import com.gov.systemservice.dto.RegisterRequest;
import com.gov.systemservice.dto.ResetPasswordRequest;
import com.gov.systemservice.pojo.User;
import com.gov.systemservice.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/api/system/user")
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
        if (request == null) {
            return Result.error("登录请求不能为空");
        }
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return Result.error("密码不能为空");
        }
        String ip = getClientIp(httpRequest);
        LoginResponse response = userService.login(request, ip);
        return Result.success(response, "登录成功");
    }

    /**
     * 用户注册
     * @param request 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody RegisterRequest request) {
        if (request == null) {
            return Result.error("注册请求不能为空");
        }
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            return Result.error("密码不能少于6位");
        }
        if (request.getRealName() == null || request.getRealName().trim().isEmpty()) {
            return Result.error("真实姓名不能为空");
        }
        User existingUser = userService.selectByUsername(request.getUsername());
        if (existingUser != null) {
            return Result.error("用户名已存在");
        }
        boolean result = userService.register(request);
        return Result.success(result);
    }

    /**
     * 密码重置
     * @param request 密码重置请求
     * @return 重置结果
     */
    @PostMapping("/resetpd")
    public Result<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        if (request == null) {
            return Result.error("密码重置请求不能为空");
        }
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (request.getOldPassword() == null || request.getOldPassword().isEmpty()) {
            return Result.error("旧密码不能为空");
        }
        if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
            return Result.error("新密码不能少于6位");
        }
        return userService.resetPassword(request);
    }

    /**
     * 获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @RequiresPermission("user:read")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    /**
     * 获取当前用户信息
     * @return 当前用户信息
     */
    @GetMapping("/info")
    @RequiresPermission("user:read")
    public Result<User> getCurrentUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        return Result.success(user);
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
