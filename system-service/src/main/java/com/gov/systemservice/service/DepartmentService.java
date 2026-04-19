package com.gov.systemservice.service;

import com.gov.systemservice.pojo.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    boolean createDepartment(Department department);
    boolean updateDepartment(Department department);
    boolean deleteDepartment(Long id);
    List<Department> getDepartmentsByParentId(Long parentId);
}
