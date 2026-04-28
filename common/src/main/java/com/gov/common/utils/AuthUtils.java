package com.gov.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一认证工具类
 * 用于从请求头中获取Gateway传递的用户信息
 *
 * @author chengbin
 * @since 2026-04-28
 */
public class AuthUtils {

    /**
     * 从请求头获取用户名
     */
    public static String getCurrentUsername() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }
        return request.getHeader("X-User-Name");
    }

    /**
     * 从请求头获取用户ID
     */
    public static String getCurrentUserId() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }
        return request.getHeader("X-User-Id");
    }

    /**
     * 从请求头获取用户真实姓名
     */
    public static String getCurrentUserRealName() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }
        return request.getHeader("X-User-RealName");
    }

    /**
     * 获取当前请求的所有用户信息
     */
    public static Map<String, String> getCurrentUserInfo() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return new HashMap<>();
        }

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", request.getHeader("X-User-Name"));
        userInfo.put("userId", request.getHeader("X-User-Id"));
        userInfo.put("realName", request.getHeader("X-User-RealName"));
        
        return userInfo;
    }

    /**
     * 获取当前HTTP请求
     */
    private static HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return null;
            }
            return attributes.getRequest();
        } catch (Exception e) {
            return null;
        }
    }
}
