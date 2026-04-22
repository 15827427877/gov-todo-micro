package com.gov.systemservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gov.common.Result;
import com.gov.common.utils.JwtUtils;
import com.gov.common.utils.LogUtils;
import com.gov.systemservice.dto.LoginRequest;
import com.gov.systemservice.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT认证过滤器
 * 用于处理登录请求，生成JWT令牌
 *
 * @author chengbin
 * @since 2026-04-20
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        // 禁用默认的登录请求路径处理，由UserController处理登录请求
        setFilterProcessesUrl("/api/login-disabled");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 从请求体中读取登录信息
            InputStream inputStream = request.getInputStream();
            LoginRequest loginRequest = new ObjectMapper().readValue(inputStream, LoginRequest.class);

            // 创建认证令牌
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );

            // 调用认证管理器进行认证
            return authenticationManager.authenticate(token);
        } catch (IOException e) {
            LogUtils.error(JwtAuthenticationFilter.class, "登录请求解析失败: {}", e.getMessage());
            throw new RuntimeException("登录请求解析失败", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        try {
            // 从认证结果中获取用户名
            String username = authResult.getName();

            // 获取客户端IP地址
            String ip = getClientIp(request);

            // 生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            String token = JwtUtils.generateToken(username, claims);

            // 构建响应
            Result<String> result = Result.success(token);
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(result));
        } catch (Exception e) {
            LogUtils.error(JwtAuthenticationFilter.class, "登录处理失败: {}", e.getMessage());
            Result<String> result = Result.error("登录处理失败: " + e.getMessage());
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(result));
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 构建错误响应
        Result<String> result = Result.error("登录失败: " + failed.getMessage());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
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