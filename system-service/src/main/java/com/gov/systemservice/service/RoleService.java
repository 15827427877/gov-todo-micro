package com.gov.systemservice.service;

import com.gov.systemservice.pojo.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    boolean createRole(Role role);
    boolean updateRole(Role role);
    boolean deleteRole(Long id);
    List<Role> getRolesByUserId(Long userId);
    boolean assignRolesToUser(Long userId, List<Long> roleIds);
    List<Long> getRolePermissions(Long roleId);
    boolean assignPermissionsToRole(Long roleId, List<Long> permissionIds);
}
