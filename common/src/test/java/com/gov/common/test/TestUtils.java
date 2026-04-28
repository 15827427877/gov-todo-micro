package com.gov.common.test;

import java.util.Random;

/**
 * 通用测试工具类
 * 提供测试数据生成和Mock对象创建方法
 *
 * @author chengbin
 * @since 2026-04-28
 */
public class TestUtils {

    private static final Random random = new Random();

    /**
     * 生成随机字符串
     */
    public static String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 生成随机邮箱
     */
    public static String randomEmail() {
        return randomString(8) + "@test.com";
    }

    /**
     * 生成随机手机号
     */
    public static String randomPhone() {
        return "1" + (30 + random.nextInt(8)) + String.format("%08d", random.nextInt(100000000));
    }

    /**
     * 生成随机Long值
     */
    public static Long randomLong() {
        return random.nextLong() & Long.MAX_VALUE;
    }

    /**
     * 生成随机Integer值
     */
    public static Integer randomInt(int bound) {
        return random.nextInt(bound);
    }
}
