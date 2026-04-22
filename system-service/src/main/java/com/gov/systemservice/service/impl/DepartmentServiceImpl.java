package com.gov.systemservice.service.impl;

import com.gov.common.utils.LogUtils;
import com.gov.systemservice.mapper.DepartmentMapper;
import com.gov.systemservice.pojo.Department;
import com.gov.systemservice.service.DepartmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> getAllDepartments() {
        return departmentMapper.selectAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentMapper.selectById(id);
    }

    @Override
    public boolean createDepartment(Department department) {
        // 计算部门级别
        if (department.getParentId() == 0) {
            department.setLevel(1);
        } else {
            Department parent = departmentMapper.selectById(department.getParentId());
            if (parent != null) {
                department.setLevel(parent.getLevel() + 1);
            } else {
                department.setLevel(1);
            }
        }

        int result = departmentMapper.insert(department);
        if (result > 0) {
            LogUtils.info(DepartmentServiceImpl.class, "创建部门成功: {}", department.getName());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateDepartment(Department department) {
        // 计算部门级别
        if (department.getParentId() == 0) {
            department.setLevel(1);
        } else {
            Department parent = departmentMapper.selectById(department.getParentId());
            if (parent != null) {
                department.setLevel(parent.getLevel() + 1);
            } else {
                department.setLevel(1);
            }
        }

        int result = departmentMapper.update(department);
        if (result > 0) {
            LogUtils.info(DepartmentServiceImpl.class, "更新部门成功: {}", department.getName());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteDepartment(Long id) {
        // 检查是否有子部门
        List<Department> children = departmentMapper.selectByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("该部门下有子部门，无法删除");
        }

        int result = departmentMapper.delete(id);
        if (result > 0) {
            LogUtils.info(DepartmentServiceImpl.class, "删除部门成功: {}", id);
            return true;
        }
        return false;
    }

    @Override
    public List<Department> getDepartmentsByParentId(Long parentId) {
        return departmentMapper.selectByParentId(parentId);
    }
}
