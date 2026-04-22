package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.pojo.Permission;
import com.gov.systemservice.service.PermissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限控制器
 * 处理权限管理相关请求
 * 
 * @author chengbin
 * @since 2026-04-19
 */
@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * 获取权限列表
     * @return 权限列表
     */
    @GetMapping("/list")
    public Result<List<Permission>> list() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return Result.success(permissions);
    }

    /**
     * 根据ID获取权限
     * @param id 权限ID
     * @return 权限信息
     */
    @GetMapping("/get/{id}")
    public Result<Permission> get(@PathVariable Long id) {
        Permission permission = permissionService.getPermissionById(id);
        return Result.success(permission);
    }

    /**
     * 创建权限
     * @param permission 权限信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public Result<Boolean> create(@RequestBody Permission permission) {
        boolean result = permissionService.createPermission(permission);
        return Result.success(result);
    }

    /**
     * 更新权限
     * @param permission 权限信息
     * @return 更新结果
     */
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody Permission permission) {
        boolean result = permissionService.updatePermission(permission);
        return Result.success(result);
    }

    /**
     * 删除权限
     * @param id 权限ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = permissionService.deletePermission(id);
        return Result.success(result);
    }

    /**
     * 获取角色的权限
     * @param roleId 角色ID
     * @return 权限列表
     */
    @GetMapping("/role/{roleId}")
    public Result<List<Permission>> getPermissionsByRoleId(@PathVariable Long roleId) {
        List<Permission> permissions = permissionService.getPermissionsByRoleId(roleId);
        return Result.success(permissions);
    }

    /**
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 分配结果
     */
    @PostMapping("/assign")
    public Result<Boolean> assignPermissionsToRole(@RequestParam Long roleId, @RequestParam List<Long> permissionIds) {
        boolean result = permissionService.assignPermissionsToRole(roleId, permissionIds);
        return Result.success(result);
    }
}
