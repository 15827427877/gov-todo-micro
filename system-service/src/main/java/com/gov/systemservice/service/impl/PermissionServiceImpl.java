package com.gov.systemservice.service.impl;

import com.gov.common.utils.LogUtils;
import com.gov.systemservice.mapper.PermissionMapper;
import com.gov.systemservice.mapper.RolePermissionMapper;
import com.gov.systemservice.pojo.Permission;
import com.gov.systemservice.pojo.RolePermission;
import com.gov.systemservice.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionMapper.selectAll();
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionMapper.selectById(id);
    }

    @Override
    public boolean createPermission(Permission permission) {
        int result = permissionMapper.insert(permission);
        if (result > 0) {
            LogUtils.info(PermissionServiceImpl.class, "创建权限成功: {}", permission.getPermissionName());
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePermission(Permission permission) {
        int result = permissionMapper.update(permission);
        if (result > 0) {
            LogUtils.info(PermissionServiceImpl.class, "更新权限成功: {}", permission.getPermissionName());
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePermission(Long id) {
        int result = permissionMapper.deleteById(id);
        if (result > 0) {
            LogUtils.info(PermissionServiceImpl.class, "删除权限成功: {}", id);
            return true;
        }
        return false;
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return permissionMapper.selectByRoleId(roleId);
    }

    @Override
    @Transactional
    public boolean assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        // 先删除角色原有的权限
        rolePermissionMapper.deleteByRoleId(roleId);

        // 为角色分配新的权限
        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionMapper.insert(rolePermission);
        }

        LogUtils.info(PermissionServiceImpl.class, "为角色分配权限成功: roleId={}, permissionIds={}", roleId, permissionIds);
        return true;
    }
}
