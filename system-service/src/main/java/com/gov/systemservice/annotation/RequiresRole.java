package com.gov.systemservice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色校验注解
 * 用于指定接口需要的角色
 *
 * @author chengbin
 * @since 2026-04-20
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    /**
     * 角色编码
     */
    String value();

    /**
     * 角色描述
     */
    String description() default "";
}