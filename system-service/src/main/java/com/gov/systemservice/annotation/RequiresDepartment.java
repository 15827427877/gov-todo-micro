package com.gov.systemservice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 部门校验注解
 * 用于指定接口需要的部门
 *
 * @author chengbin
 * @since 2026-04-20
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresDepartment {
    /**
     * 部门ID
     */
    long value();

    /**
     * 部门描述
     */
    String description() default "";
}