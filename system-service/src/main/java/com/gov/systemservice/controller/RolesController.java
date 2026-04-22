package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.annotation.RequiresPermission;
import com.gov.systemservice.pojo.Role;
import com.gov.systemservice.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色管理控制器
 * 处理角色列表、创建、更新、删除等请求
 *
 * @author chengbin
 * @since 2026-04-20
 */
@RestController
@RequestMapping("/api/roles")
public class RolesController {

    @Resource
    private RoleService roleService;

    /**
     * 获取角色列表
     * @return 角色列表
     */
    @GetMapping
    @RequiresPermission("role:list")
    public Result<List<Role>> getRoles() {
        List<Role> roles = roleService.getAllRoles();
        return Result.success(roles, "操作成功");
    }

    /**
     * 获取角色详情
     * @param id 角色ID
     * @return 角色详情
     */
    @GetMapping("/{id}")
    @RequiresPermission("role:read")
    public Result<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return Result.success(role, "操作成功");
    }

    /**
     * 创建角色
     * @param role 角色信息
     * @return 创建结果
     */
    @PostMapping
    @RequiresPermission("role:create")
    public Result<Role> createRole(@RequestBody Role role) {
        boolean created = roleService.createRole(role);
        if (created) {
            return Result.success(role, "新增成功");
        }
        return Result.error("创建角色失败");
    }

    /**
     * 更新角色
     * @param id 角色ID
     * @param role 角色信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @RequiresPermission("role:update")
    public Result<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);
        boolean updated = roleService.updateRole(role);
        if (updated) {
            return Result.success(role, "更新成功");
        }
        return Result.error("更新角色失败");
    }

    /**
     * 删除角色
     * @param id 角色ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @RequiresPermission("role:delete")
    public Result<Boolean> deleteRole(@PathVariable Long id) {
        boolean deleted = roleService.deleteRole(id);
        return Result.success(deleted, "删除成功");
    }

    /**
     * 获取角色权限
     * @param id 角色ID
     * @return 角色权限
     */
    @GetMapping("/{id}/permissions")
    @RequiresPermission("role:read")
    public Result<List<Long>> getRolePermissions(@PathVariable Long id) {
        List<Long> permissionIds = roleService.getRolePermissions(id);
        return Result.success(permissionIds, "操作成功");
    }

    /**
     * 分配权限
     * @param id 角色ID
     * @param request 包含权限ID列表的请求对象
     * @return 分配结果
     */
    @PostMapping("/{id}/permissions")
    @RequiresPermission("role:update")
    public Result<Boolean> assignPermissions(@PathVariable Long id, @RequestBody PermissionIdsRequest request) {
        boolean result = roleService.assignPermissionsToRole(id, request.getPermissionIds());
        return Result.success(result, "权限分配成功");
    }
    
    /**
     * 权限ID列表请求对象
     */
    static class PermissionIdsRequest {
        private List<Long> permissionIds;
        
        public List<Long> getPermissionIds() {
            return permissionIds;
        }
        
        public void setPermissionIds(List<Long> permissionIds) {
            this.permissionIds = permissionIds;
        }
    }
}
