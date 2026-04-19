package com.gov.systemservice.controller;

import com.gov.systemservice.pojo.Department;
import com.gov.systemservice.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DepartmentControllerTest {

    @InjectMocks
    private DepartmentController departmentController;

    @Mock
    private DepartmentService departmentService;

    @Test
    public void testList() {
        List<Department> departments = new ArrayList<>();
        Department department = new Department();
        department.setId(1L);
        department.setDepartmentName("总部门");
        department.setParentId(0L);
        department.setLevel(1);
        departments.add(department);

        // Mock departmentService.getAllDepartments() method
        // when(departmentService.getAllDepartments()).thenReturn(departments);

        // ResponseEntity<Result<List<Department>>> response = departmentController.list();
        // assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGet() {
        Department department = new Department();
        department.setId(1L);
        department.setDepartmentName("总部门");
        department.setParentId(0L);
        department.setLevel(1);

        // Mock departmentService.getDepartmentById() method
        // when(departmentService.getDepartmentById(1L)).thenReturn(department);

        // ResponseEntity<Result<Department>> response = departmentController.get(1L);
        // assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
