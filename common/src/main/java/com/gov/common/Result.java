package com.gov.common;

import java.io.Serializable;

/**
 * 统一返回结果类
 * 用于封装API响应结果，包含状态码、消息和数据
 * 
 * @author chengbin
 * @since 2026-04-19
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private T data;
    private long timestamp;

    /**
     * 构造方法
     */
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应（无数据）
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功响应（带数据）
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMsg("Success");
        r.setData(data);
        return r;
    }

    /**
     * 错误响应
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> error(String msg) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }

    /**
     * 错误响应（带状态码）
     * @param code 状态码
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> error(int code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    /**
     * 400错误响应
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 400错误响应结果
     */
    public static <T> Result<T> badRequest(String msg) {
        return error(400, msg);
    }

    /**
     * 401错误响应
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 401错误响应结果
     */
    public static <T> Result<T> unauthorized(String msg) {
        return error(401, msg);
    }

    /**
     * 403错误响应
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 403错误响应结果
     */
    public static <T> Result<T> forbidden(String msg) {
        return error(403, msg);
    }

    /**
     * 404错误响应
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 404错误响应结果
     */
    public static <T> Result<T> notFound(String msg) {
        return error(404, msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 判断是否成功
     * @return 是否成功
     */
    public boolean isSuccess() {
        return code == 200;
    }
}
