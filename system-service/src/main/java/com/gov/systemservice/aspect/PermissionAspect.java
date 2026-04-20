package com.gov.systemservice.aspect;

import com.gov.common.Result;
import com.gov.systemservice.annotation.RequiresDepartment;
import com.gov.systemservice.annotation.RequiresPermission;
import com.gov.systemservice.annotation.RequiresRole;
import com.gov.systemservice.utils.PermissionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 权限校验切面
 * 用于拦截带有权限、角色或部门校验注解的方法，并进行权限校验
 *
 * @author chengbin
 * @since 2026-04-20
 */
@Aspect
@Component
public class PermissionAspect {

    @Resource
    private PermissionUtils permissionUtils;

    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.gov.systemservice.annotation.RequiresPermission) || @annotation(com.gov.systemservice.annotation.RequiresRole) || @annotation(com.gov.systemservice.annotation.RequiresDepartment)")
    public void permissionPointcut() {
    }

    /**
     * 前置通知，用于权限校验
     */
    @Before("permissionPointcut()")
    public void before(JoinPoint joinPoint) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 检查是否有RequiresPermission注解
        if (method.isAnnotationPresent(RequiresPermission.class)) {
            RequiresPermission annotation = method.getAnnotation(RequiresPermission.class);
            String permissionCode = annotation.value();
            if (!permissionUtils.hasPermission(permissionCode)) {
                throw new RuntimeException("无权限访问该接口");
            }
        }

        // 检查是否有RequiresRole注解
        if (method.isAnnotationPresent(RequiresRole.class)) {
            RequiresRole annotation = method.getAnnotation(RequiresRole.class);
            String roleCode = annotation.value();
            if (!permissionUtils.hasRole(roleCode)) {
                throw new RuntimeException("无权限访问该接口");
            }
        }

        // 检查是否有RequiresDepartment注解
        if (method.isAnnotationPresent(RequiresDepartment.class)) {
            RequiresDepartment annotation = method.getAnnotation(RequiresDepartment.class);
            long departmentId = annotation.value();
            if (!permissionUtils.isInDepartment(departmentId)) {
                throw new RuntimeException("无权限访问该接口");
            }
        }
    }
}