package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.pojo.Role;
import com.gov.systemservice.service.RoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色控制器
 * 处理角色管理相关请求
 * 
 * @author chengbin
 * @since 2026-04-19
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 获取角色列表
     * @return 角色列表
     */
    @GetMapping("/list")
    public Result<List<Role>> list() {
        List<Role> roles = roleService.getAllRoles();
        return Result.success(roles);
    }

    /**
     * 根据ID获取角色
     * @param id 角色ID
     * @return 角色信息
     */
    @GetMapping("/get/{id}")
    public Result<Role> get(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    /**
     * 创建角色
     * @param role 角色信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public Result<Boolean> create(@RequestBody Role role) {
        boolean result = roleService.createRole(role);
        return Result.success(result);
    }

    /**
     * 更新角色
     * @param role 角色信息
     * @return 更新结果
     */
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody Role role) {
        boolean result = roleService.updateRole(role);
        return Result.success(result);
    }

    /**
     * 删除角色
     * @param id 角色ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = roleService.deleteRole(id);
        return Result.success(result);
    }

    /**
     * 获取用户的角色
     * @param userId 用户ID
     * @return 角色列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<Role>> getRolesByUserId(@PathVariable Long userId) {
        List<Role> roles = roleService.getRolesByUserId(userId);
        return Result.success(roles);
    }

    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 分配结果
     */
    @PostMapping("/assign")
    public Result<Boolean> assignRolesToUser(@RequestParam Long userId, @RequestBody List<Long> roleIds) {
        boolean result = roleService.assignRolesToUser(userId, roleIds);
        return Result.success(result);
    }
}
