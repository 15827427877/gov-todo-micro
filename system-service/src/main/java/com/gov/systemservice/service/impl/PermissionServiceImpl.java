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
    public List<Permission> getPermissionsTree() {
        // 获取所有权限
        List<Permission> allPermissions = permissionMapper.selectAll();
        // 构建树形结构
        return buildPermissionTree(allPermissions);
    }

    /**
     * 构建权限树形结构
     * @param permissions 所有权限列表
     * @return 树形结构的权限列表
     */
    private List<Permission> buildPermissionTree(List<Permission> permissions) {
        List<Permission> rootPermissions = new java.util.ArrayList<>();
        
        // 先找出所有根权限（parentId为null或0）
        for (Permission permission : permissions) {
            if (permission.getParentId() == null || permission.getParentId() == 0) {
                rootPermissions.add(permission);
            }
        }
        
        // 为每个根权限添加子权限
        for (Permission rootPermission : rootPermissions) {
            rootPermission.setChildren(findChildren(rootPermission.getId(), permissions));
        }
        
        return rootPermissions;
    }

    /**
     * 查找子权限
     * @param parentId 父权限ID
     * @param permissions 所有权限列表
     * @return 子权限列表
     */
    private List<Permission> findChildren(Long parentId, List<Permission> permissions) {
        List<Permission> children = new java.util.ArrayList<>();
        
        for (Permission permission : permissions) {
            if (permission.getParentId() != null && permission.getParentId().equals(parentId)) {
                permission.setChildren(findChildren(permission.getId(), permissions));
                children.add(permission);
            }
        }
        
        return children;
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
        try {
            // 先删除角色原有的权限
            rolePermissionMapper.deleteByRoleId(roleId);

            // 为角色分配新的权限
            if (permissionIds != null && !permissionIds.isEmpty()) {
                for (Long permissionId : permissionIds) {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionId(permissionId);
                    rolePermissionMapper.insert(rolePermission);
                }
            }

            LogUtils.info(PermissionServiceImpl.class, "为角色分配权限成功: roleId={}, permissionIds={}", roleId, permissionIds);
            return true;
        } catch (Exception e) {
            LogUtils.error(PermissionServiceImpl.class, "为角色分配权限失败: roleId={}, error={}", roleId, e.getMessage());
            throw new RuntimeException("权限分配失败: " + e.getMessage());
        }
    }
}
