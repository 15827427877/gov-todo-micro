package com.gov.common.utils;

/**
 * 数据脱敏工具类
 * 用于对敏感信息进行脱敏处理
 * 
 * @author chengbin
 * @since 2026-04-19
 */
public class SensitiveUtils {

    /**
     * 私有构造方法，防止实例化
     */
    private SensitiveUtils() {
    }

    /**
     * 手机号脱敏
     * @param phone 手机号
     * @return 脱敏后的手机号
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 邮箱脱敏
     * @param email 邮箱
     * @return 脱敏后的邮箱
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String username = parts[0];
        if (username.length() <= 3) {
            return username + "****@" + parts[1];
        }
        return username.substring(0, 3) + "****" + "@" + parts[1];
    }

    /**
     * 身份证号脱敏
     * @param idCard 身份证号
     * @return 脱敏后的身份证号
     */
    public static String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 15) {
            return idCard;
        }
        if (idCard.length() == 15) {
            return idCard.substring(0, 6) + "****" + idCard.substring(11);
        }
        return idCard.substring(0, 6) + "****" + idCard.substring(14);
    }

    /**
     * 姓名脱敏
     * @param name 姓名
     * @return 脱敏后的姓名
     */
    public static String maskName(String name) {
        if (name == null || name.length() <= 1) {
            return name;
        }
        if (name.length() == 2) {
            return name.charAt(0) + "*";
        }
        if (name.length() == 3) {
            return name.charAt(0) + "*" + name.charAt(2);
        }
        return name.charAt(0) + "****" + name.substring(name.length() - 1);
    }

    /**
     * 地址脱敏
     * @param address 地址
     * @return 脱敏后的地址
     */
    public static String maskAddress(String address) {
        if (address == null || address.length() <= 10) {
            return address;
        }
        return address.substring(0, 10) + "****";
    }

    /**
     * 银行卡号脱敏
     * @param bankCard 银行卡号
     * @return 脱敏后的银行卡号
     */
    public static String maskBankCard(String bankCard) {
        if (bankCard == null || bankCard.length() < 16) {
            return bankCard;
        }
        return bankCard.substring(0, 4) + " **** **** " + bankCard.substring(bankCard.length() - 4);
    }

    /**
     * 通用脱敏方法
     * @param text 原始文本
     * @param start 开始位置
     * @param end 结束位置
     * @return 脱敏后的文本
     */
    public static String mask(String text, int start, int end) {
        if (text == null || text.length() <= start || end <= start) {
            return text;
        }
        if (end > text.length()) {
            end = text.length();
        }
        StringBuilder sb = new StringBuilder(text);
        for (int i = start; i < end; i++) {
            sb.setCharAt(i, '*');
        }
        return sb.toString();
    }
}
