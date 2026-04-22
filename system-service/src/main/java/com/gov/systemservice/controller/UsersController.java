package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.annotation.RequiresPermission;
import com.gov.systemservice.pojo.User;
import com.gov.systemservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户管理控制器
 * 处理用户列表、创建、更新、删除等请求
 *
 * @author chengbin
 * @since 2026-04-20
 */
@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Resource
    private UserService userService;

    /**
     * 获取用户列表
     * @return 用户列表
     */
    @GetMapping
    @RequiresPermission("user:list")
    public Result<List<User>> getUsers() {
        List<User> users = userService.getUsers();
        return Result.success(users);
    }

    /**
     * 获取用户详情
     * @param id 用户ID
     * @return 用户详情
     */
    @GetMapping("/{id}")
    @RequiresPermission("user:read")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    /**
     * 创建用户
     * @param user 用户信息
     * @return 创建结果
     */
    @PostMapping
    @RequiresPermission("user:create")
    public Result<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return Result.success(createdUser);
    }

    /**
     * 更新用户
     * @param id 用户ID
     * @param user 用户信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @RequiresPermission("user:update")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return Result.success(updatedUser);
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @RequiresPermission("user:delete")
    public Result<Boolean> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return Result.success(deleted);
    }

    /**
     * 获取用户角色列表
     * @param id 用户ID
     * @return 角色ID列表
     */
    @GetMapping("/{id}/roles")
    @RequiresPermission("user:read")
    public Result<List<Long>> getUserRoles(@PathVariable Long id) {
        List<Long> roleIds = userService.getUserRoles(id);
        return Result.success(roleIds);
    }

    /**
     * 为用户分配角色
     * @param id 用户ID
     * @param roleIds 角色ID列表
     * @return 分配结果
     */
    @PostMapping("/{id}/roles")
    @RequiresPermission("user:update")
    public Result<Boolean> assignRolesToUser(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        boolean result = userService.assignRolesToUser(id, roleIds);
        return Result.success(result, "角色分配成功");
    }
}
