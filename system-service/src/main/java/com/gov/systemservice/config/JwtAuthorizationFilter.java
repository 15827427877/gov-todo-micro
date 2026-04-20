package com.gov.systemservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gov.common.Result;
import com.gov.common.utils.JwtUtils;
import com.gov.common.utils.LogUtils;
import com.gov.systemservice.pojo.User;
import com.gov.systemservice.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT授权过滤器
 * 用于验证JWT令牌，提取用户信息并设置到SecurityContext中
 *
 * @author chengbin
 * @since 2026-04-20
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private UserService userService;

    public JwtAuthorizationFilter() {
    }

    public JwtAuthorizationFilter(UserService userService) {
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            // 从请求头中获取令牌
            String token = request.getHeader("Authorization");
            LogUtils.info(JwtAuthorizationFilter.class, "请求头中的令牌: {}", token);
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                LogUtils.info(JwtAuthorizationFilter.class, "提取后的令牌: {}", token);

                // 解析令牌，获取用户信息
                Claims claims = JwtUtils.parseToken(token);
                LogUtils.info(JwtAuthorizationFilter.class, "解析后的令牌: {}", claims);
                if (claims != null) {
                    String username = claims.getSubject();
                    LogUtils.info(JwtAuthorizationFilter.class, "从令牌中获取的用户名: {}", username);

                    // 获取用户信息
                    User user = userService.getUserByUsername(username);
                    LogUtils.info(JwtAuthorizationFilter.class, "从数据库中获取的用户信息: {}", user);
                    if (user != null) {
                        // 获取用户角色和权限
                        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        // 添加默认权限
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                        LogUtils.info(JwtAuthorizationFilter.class, "用户的权限: {}", authorities);

                        // 创建认证令牌
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                authorities
                        );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        LogUtils.info(JwtAuthorizationFilter.class, "创建的认证令牌: {}", authentication);

                        // 设置到SecurityContext中
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        LogUtils.info(JwtAuthorizationFilter.class, "设置到SecurityContext中的认证令牌: {}", SecurityContextHolder.getContext().getAuthentication());
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.error(JwtAuthorizationFilter.class, "JWT授权失败: {}", e.getMessage());
        }

        // 继续执行过滤器链
        chain.doFilter(request, response);
    }
}