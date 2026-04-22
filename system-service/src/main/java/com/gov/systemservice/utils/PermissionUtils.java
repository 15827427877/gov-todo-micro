package com.gov.systemservice.utils;

import com.gov.common.utils.LogUtils;
import com.gov.systemservice.mapper.*;
import com.gov.systemservice.pojo.Permission;
import com.gov.systemservice.pojo.Role;
import com.gov.systemservice.pojo.User;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限验证工具类
 * 用于检查用户是否具有特定的权限
 *
 * @author chengbin
 * @since 2026-04-20
 */
@Component
public class PermissionUtils {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 检查用户是否具有特定的权限
     * @param permissionCode 权限编码
     * @return 是否具有权限
     */
    public boolean hasPermission(String permissionCode) {
        try {
            // 从SecurityContext中获取用户名
            org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }
            String username = authentication.getName();

            // 测试环境：为了方便测试，让admin用户拥有所有权限
            if ("admin".equals(username)) {
                return true;
            }

            // 获取用户信息
            User user = userMapper.selectByUsername(username);
            if (user == null) {
                return false;
            }

            // 获取用户角色
            List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(user.getId());
            if (roleIds.isEmpty()) {
                return false;
            }

            // 检查每个角色是否具有指定权限
            for (Long roleId : roleIds) {
                List<Long> permissionIds = rolePermissionMapper.selectPermissionIdsByRoleId(roleId);
                for (Long permissionId : permissionIds) {
                    Permission permission = permissionMapper.selectById(permissionId);
                    if (permission != null && permissionCode.equals(permission.getPermissionCode())) {
                        return true;
                    }
                }
            }

            return false;
        } catch (Exception e) {
            LogUtils.error(PermissionUtils.class, "权限检查失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查用户是否属于特定部门
     * @param departmentId 部门ID
     * @return 是否属于该部门
     */
    public boolean isInDepartment(Long departmentId) {
        try {
            // 从SecurityContext中获取用户名
            org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }
            String username = authentication.getName();

            // 获取用户信息
            User user = userMapper.selectByUsername(username);
            if (user == null) {
                return false;
            }

            // 检查用户部门ID是否匹配
            return user.getDepartmentId().equals(departmentId);
        } catch (Exception e) {
            LogUtils.error(PermissionUtils.class, "部门检查失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查用户是否具有特定角色
     * @param roleCode 角色编码
     * @return 是否具有该角色
     */
    public boolean hasRole(String roleCode) {
        try {
            // 从SecurityContext中获取用户名
            org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }
            String username = authentication.getName();

            // 获取用户信息
            User user = userMapper.selectByUsername(username);
            if (user == null) {
                return false;
            }

            // 获取用户角色
            List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(user.getId());
            if (roleIds.isEmpty()) {
                return false;
            }

            // 检查每个角色是否匹配
            for (Long roleId : roleIds) {
                Role role = roleMapper.selectById(roleId);
                if (role != null && roleCode.equals(role.getRoleCode())) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            LogUtils.error(PermissionUtils.class, "角色检查失败: {}", e.getMessage());
            return false;
        }
    }
}