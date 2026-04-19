package com.gov.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 * 提供不同级别的日志记录功能
 * 
 * @author chengbin
 * @since 2026-04-19
 */
public class LogUtils {

    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    /**
     * 私有构造方法，防止实例化
     */
    private LogUtils() {
    }

    /**
     * 获取Logger实例
     * @param clazz 类对象
     * @return Logger实例
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * 调试级别日志
     * @param message 日志消息
     */
    public static void debug(String message) {
        logger.debug(message);
    }

    /**
     * 调试级别日志（带参数）
     * @param message 日志消息
     * @param args 参数
     */
    public static void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    /**
     * 调试级别日志（指定类）
     * @param clazz 类对象
     * @param message 日志消息
     */
    public static void debug(Class<?> clazz, String message) {
        LoggerFactory.getLogger(clazz).debug(message);
    }

    /**
     * 调试级别日志（指定类，带参数）
     * @param clazz 类对象
     * @param message 日志消息
     * @param args 参数
     */
    public static void debug(Class<?> clazz, String message, Object... args) {
        LoggerFactory.getLogger(clazz).debug(message, args);
    }

    /**
     * 信息级别日志
     * @param message 日志消息
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * 信息级别日志（带参数）
     * @param message 日志消息
     * @param args 参数
     */
    public static void info(String message, Object... args) {
        logger.info(message, args);
    }

    /**
     * 信息级别日志（指定类）
     * @param clazz 类对象
     * @param message 日志消息
     */
    public static void info(Class<?> clazz, String message) {
        LoggerFactory.getLogger(clazz).info(message);
    }

    /**
     * 信息级别日志（指定类，带参数）
     * @param clazz 类对象
     * @param message 日志消息
     * @param args 参数
     */
    public static void info(Class<?> clazz, String message, Object... args) {
        LoggerFactory.getLogger(clazz).info(message, args);
    }

    /**
     * 警告级别日志
     * @param message 日志消息
     */
    public static void warn(String message) {
        logger.warn(message);
    }

    /**
     * 警告级别日志（带参数）
     * @param message 日志消息
     * @param args 参数
     */
    public static void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    /**
     * 警告级别日志（指定类）
     * @param clazz 类对象
     * @param message 日志消息
     */
    public static void warn(Class<?> clazz, String message) {
        LoggerFactory.getLogger(clazz).warn(message);
    }

    /**
     * 警告级别日志（指定类，带参数）
     * @param clazz 类对象
     * @param message 日志消息
     * @param args 参数
     */
    public static void warn(Class<?> clazz, String message, Object... args) {
        LoggerFactory.getLogger(clazz).warn(message, args);
    }

    /**
     * 错误级别日志
     * @param message 日志消息
     */
    public static void error(String message) {
        logger.error(message);
    }

    /**
     * 错误级别日志（带异常）
     * @param message 日志消息
     * @param e 异常
     */
    public static void error(String message, Throwable e) {
        logger.error(message, e);
    }

    /**
     * 错误级别日志（带参数）
     * @param message 日志消息
     * @param args 参数
     */
    public static void error(String message, Object... args) {
        logger.error(message, args);
    }

    /**
     * 错误级别日志（指定类）
     * @param clazz 类对象
     * @param message 日志消息
     */
    public static void error(Class<?> clazz, String message) {
        LoggerFactory.getLogger(clazz).error(message);
    }

    /**
     * 错误级别日志（指定类，带异常）
     * @param clazz 类对象
     * @param message 日志消息
     * @param e 异常
     */
    public static void error(Class<?> clazz, String message, Throwable e) {
        LoggerFactory.getLogger(clazz).error(message, e);
    }

    /**
     * 错误级别日志（指定类，带参数）
     * @param clazz 类对象
     * @param message 日志消息
     * @param args 参数
     */
    public static void error(Class<?> clazz, String message, Object... args) {
        LoggerFactory.getLogger(clazz).error(message, args);
    }

    /**
     * 业务操作日志
     * @param clazz 类对象
     * @param operation 操作内容
     * @param userId 用户ID
     * @param ip IP地址
     */
    public static void business(Class<?> clazz, String operation, String userId, String ip) {
        LoggerFactory.getLogger(clazz).info("[业务操作] 用户:{} IP:{} 操作:{} 时间:{}", 
            userId, ip, operation, System.currentTimeMillis());
    }

    /**
     * 安全日志
     * @param clazz 类对象
     * @param event 事件
     * @param userId 用户ID
     * @param ip IP地址
     */
    public static void security(Class<?> clazz, String event, String userId, String ip) {
        LoggerFactory.getLogger(clazz).info("[安全事件] 用户:{} IP:{} 事件:{} 时间:{}", 
            userId, ip, event, System.currentTimeMillis());
    }
}
