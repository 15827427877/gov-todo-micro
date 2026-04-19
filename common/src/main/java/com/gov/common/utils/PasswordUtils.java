package com.gov.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码工具类
 * 用于密码加密和验证
 * 
 * @author chengbin
 * @since 2026-04-19
 */
public class PasswordUtils {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 私有构造方法，防止实例化
     */
    private PasswordUtils() {
    }

    /**
     * 加密密码
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 生成随机密码
     * @param length 密码长度
     * @return 随机密码
     */
    public static String generateRandomPassword(int length) {
        if (length < 6) {
            length = 6;
        }
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }

    /**
     * 检查密码强度
     * @param password 密码
     * @return 密码强度等级（1-弱，2-中，3-强）
     */
    public static int checkPasswordStrength(String password) {
        if (password == null || password.length() < 6) {
            return 1; // 弱
        }

        int strength = 1;
        if (password.length() >= 8) {
            strength++;
        }
        if (password.matches(".*[A-Z].*")) {
            strength++;
        }
        if (password.matches(".*[a-z].*")) {
            strength++;
        }
        if (password.matches(".*[0-9].*")) {
            strength++;
        }
        if (password.matches(".*[!@#$%^&*].*")) {
            strength++;
        }

        if (strength < 3) {
            return 1; // 弱
        } else if (strength < 5) {
            return 2; // 中
        } else {
            return 3; // 强
        }
    }
}
