package com.gov.gatewayserver.filter;

import com.gov.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Gateway统一认证过滤器
 * 负责JWT令牌验证,将用户信息传递到下游服务
 *
 * @author chengbin
 * @since 2026-04-28
 */
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    /**
     * 认证白名单,不需要认证的接口
     */
    @Value("${auth.whitelist:/api/login,/api/register,/api/system/user/login,/api/system/user/register,/api/system/user/resetpd}")
    private String whitelistPaths;

    private final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        log.debug("AuthGlobalFilter processing request: {}", path);

        // 检查是否在白名单中
        if (isWhitelist(path)) {
            log.debug("Path {} is in whitelist, skip authentication", path);
            return chain.filter(exchange);
        }

        // 获取Authorization头
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        // 验证令牌
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for path: {}", path);
            return unauthorizedResponse(exchange, "未提供认证令牌");
        }

        String token = authHeader.substring(7);
        
        // 验证JWT令牌
        if (!JwtUtils.validateToken(token)) {
            log.warn("Invalid JWT token for path: {}", path);
            return unauthorizedResponse(exchange, "认证令牌无效或已过期");
        }

        // 解析令牌获取用户信息
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            log.warn("Failed to parse JWT token for path: {}", path);
            return unauthorizedResponse(exchange, "认证令牌解析失败");
        }

        String username = claims.getSubject();
        Object userIdObj = claims.get("userId");
        Object realNameObj = claims.get("realName");

        String userId = userIdObj != null ? String.valueOf(userIdObj) : "";
        String realName = realNameObj != null ? String.valueOf(realNameObj) : "";

        log.debug("Authentication successful for user: {}, path: {}", username, path);

        // 将用户信息添加到请求头,传递给下游服务
        ServerHttpRequest.Builder requestBuilder = request.mutate();

        // 安全地添加header，避免Netty验证失败
        if (username != null && !username.isEmpty()) {
            requestBuilder.header("X-User-Name", safeHeaderValue(username));
        }
        if (userId != null && !userId.isEmpty()) {
            requestBuilder.header("X-User-Id", safeHeaderValue(userId));
        }
        if (realName != null && !realName.isEmpty()) {
            requestBuilder.header("X-User-RealName", safeHeaderValue(realName));
        }

        ServerHttpRequest mutatedRequest = requestBuilder.build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        // 设置为-100,确保在Sentinel过滤器之后执行
        return -100;
    }

    /**
     * 检查路径是否在白名单中
     */
    private boolean isWhitelist(String path) {
        if (whitelistPaths == null || whitelistPaths.isEmpty()) {
            return false;
        }
        
        String[] paths = whitelistPaths.split(",");
        for (String whitelistPath : paths) {
            String trimmedPath = whitelistPath.trim();
            if (pathMatcher.match(trimmedPath, path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回未授权响应
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        String body = String.format("{\"code\":401,\"message\":\"%s\"}", message);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 安全地处理header值，确保符合Netty验证要求
     */
    private String safeHeaderValue(String value) {
        if (value == null) {
            return "";
        }
        // 只保留可打印ASCII字符
        StringBuilder sb = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (c >= 32 && c < 127) {
                sb.append(c);
            }
        }
        String result = sb.toString().trim();
        // 如果结果为空，返回默认值
        return result.isEmpty() ? "unknown" : result;
    }
}
