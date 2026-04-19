package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.pojo.Department;
import com.gov.systemservice.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门控制器
 * 处理部门管理相关请求
 * 
 * @author chengbin
 * @since 2026-04-19
 */
@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    /**
     * 获取部门列表
     * @return 部门列表
     */
    @GetMapping("/list")
    public Result<List<Department>> list() {
        List<Department> departments = departmentService.getAllDepartments();
        return Result.success(departments);
    }

    /**
     * 根据ID获取部门
     * @param id 部门ID
     * @return 部门信息
     */
    @GetMapping("/get/{id}")
    public Result<Department> get(@PathVariable Long id) {
        Department department = departmentService.getDepartmentById(id);
        return Result.success(department);
    }

    /**
     * 创建部门
     * @param department 部门信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public Result<Boolean> create(@RequestBody Department department) {
        boolean result = departmentService.createDepartment(department);
        return Result.success(result);
    }

    /**
     * 更新部门
     * @param department 部门信息
     * @return 更新结果
     */
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody Department department) {
        boolean result = departmentService.updateDepartment(department);
        return Result.success(result);
    }

    /**
     * 删除部门
     * @param id 部门ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = departmentService.deleteDepartment(id);
        return Result.success(result);
    }

    /**
     * 获取子部门
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    @GetMapping("/children/{parentId}")
    public Result<List<Department>> getChildren(@PathVariable Long parentId) {
        List<Department> departments = departmentService.getDepartmentsByParentId(parentId);
        return Result.success(departments);
    }
}
