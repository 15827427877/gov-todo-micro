package com.gov.systemservice.service;

import com.gov.systemservice.pojo.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> getAllPermissions();
    Permission getPermissionById(Long id);
    boolean createPermission(Permission permission);
    boolean updatePermission(Permission permission);
    boolean deletePermission(Long id);
    List<Permission> getPermissionsByRoleId(Long roleId);
    boolean assignPermissionsToRole(Long roleId, List<Long> permissionIds);
}
