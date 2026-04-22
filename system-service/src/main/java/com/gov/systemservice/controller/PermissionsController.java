package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.annotation.RequiresPermission;
import com.gov.systemservice.pojo.Permission;
import com.gov.systemservice.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限管理控制器
 * 处理权限列表、创建、更新、删除等请求
 *
 * @author chengbin
 * @since 2026-04-20
 */
@RestController
@RequestMapping("/api/permissions")
public class PermissionsController {

    @Resource
    private PermissionService permissionService;

    /**
     * 获取权限列表
     * @return 权限列表
     */
    @GetMapping
    @RequiresPermission("permission:list")
    public Result<List<Permission>> getPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return Result.success(permissions, "操作成功");
    }

    /**
     * 获取权限树形结构
     * @return 权限树形结构
     */
    @GetMapping("/tree")
    @RequiresPermission("permission:list")
    public Result<List<Permission>> getPermissionsTree() {
        List<Permission> permissionsTree = permissionService.getPermissionsTree();
        return Result.success(permissionsTree, "操作成功");
    }

    /**
     * 获取权限详情
     * @param id 权限ID
     * @return 权限详情
     */
    @GetMapping("/{id}")
    @RequiresPermission("permission:read")
    public Result<Permission> getPermissionById(@PathVariable Long id) {
        Permission permission = permissionService.getPermissionById(id);
        return Result.success(permission, "操作成功");
    }

    /**
     * 创建权限
     * @param permission 权限信息
     * @return 创建结果
     */
    @PostMapping
    @RequiresPermission("permission:create")
    public Result<Permission> createPermission(@RequestBody Permission permission) {
        boolean created = permissionService.createPermission(permission);
        if (created) {
            return Result.success(permission, "新增成功");
        }
        return Result.error("创建权限失败");
    }

    /**
     * 更新权限
     * @param id 权限ID
     * @param permission 权限信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @RequiresPermission("permission:update")
    public Result<Permission> updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        permission.setId(id);
        boolean updated = permissionService.updatePermission(permission);
        if (updated) {
            return Result.success(permission, "更新成功");
        }
        return Result.error("更新权限失败");
    }

    /**
     * 删除权限
     * @param id 权限ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @RequiresPermission("permission:delete")
    public Result<Boolean> deletePermission(@PathVariable Long id) {
        boolean deleted = permissionService.deletePermission(id);
        return Result.success(deleted, "删除成功");
    }

    /**
     * 获取模块列表
     * @return 模块列表
     */
    @GetMapping("/modules")
    @RequiresPermission("permission:list")
    public Result<List<String>> getModules() {
        // 这里需要实现获取模块列表的逻辑
        // 暂时返回默认模块列表，后续需要实现
        return Result.success(List.of("待办管理", "用户管理", "角色管理", "权限管理", "部门管理"));
    }
}
