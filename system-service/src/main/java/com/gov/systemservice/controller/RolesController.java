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
        return Result.success(roles);
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
            return Result.success(role);
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
            return Result.success(role);
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
        return Result.success(deleted);
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
        return Result.success(permissionIds);
    }

    /**
     * 分配权限
     * @param id 角色ID
     * @param permissionIds 权限ID列表
     * @return 分配结果
     */
    @PostMapping("/{id}/permissions")
    @RequiresPermission("role:update")
    public Result<Boolean> assignPermissions(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        boolean result = roleService.assignPermissionsToRole(id, permissionIds);
        return Result.success(result);
    }
}
