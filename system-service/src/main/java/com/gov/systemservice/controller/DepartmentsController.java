package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.annotation.RequiresPermission;
import com.gov.systemservice.pojo.Department;
import com.gov.systemservice.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门管理控制器
 * 处理部门列表、创建、更新、删除等请求
 *
 * @author chengbin
 * @since 2026-04-20
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentsController {

    @Resource
    private DepartmentService departmentService;

    /**
     * 获取部门列表
     * @return 部门列表
     */
    @GetMapping
    @RequiresPermission("department:list")
    public Result<List<Department>> getDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return Result.success(departments);
    }

    /**
     * 创建部门
     * @param department 部门信息
     * @return 创建结果
     */
    @PostMapping
    @RequiresPermission("department:create")
    public Result<Department> createDepartment(@RequestBody Department department) {
        boolean created = departmentService.createDepartment(department);
        if (created) {
            return Result.success(department);
        }
        return Result.error("创建部门失败");
    }

    /**
     * 更新部门
     * @param id 部门ID
     * @param department 部门信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @RequiresPermission("department:update")
    public Result<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        department.setId(id);
        boolean updated = departmentService.updateDepartment(department);
        if (updated) {
            return Result.success(department);
        }
        return Result.error("更新部门失败");
    }

    /**
     * 删除部门
     * @param id 部门ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @RequiresPermission("department:delete")
    public Result<Boolean> deleteDepartment(@PathVariable Long id) {
        boolean deleted = departmentService.deleteDepartment(id);
        return Result.success(deleted);
    }
}
