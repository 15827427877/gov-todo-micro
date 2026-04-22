package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.annotation.RequiresPermission;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模块控制器
 * 处理模块列表相关请求
 *
 * @author chengbin
 * @since 2026-04-22
 */
@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    /**
     * 获取模块列表
     * @return 模块列表
     */
    @GetMapping
    @RequiresPermission("permission:list")
    public Result<List<String>> getModules() {
        // 返回默认模块列表
        return Result.success(List.of("待办管理", "用户管理", "角色管理", "权限管理", "部门管理"));
    }
}
