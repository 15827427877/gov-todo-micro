package com.gov.systemservice.controller;

import com.gov.systemservice.pojo.Role;
import com.gov.systemservice.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

    @Test
    public void testList() {
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("管理员");
        role.setRoleCode("ADMIN");
        roles.add(role);

        // Mock roleService.getAllRoles() method
        // when(roleService.getAllRoles()).thenReturn(roles);

        // ResponseEntity<Result<List<Role>>> response = roleController.list();
        // assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGet() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("管理员");
        role.setRoleCode("ADMIN");

        // Mock roleService.getRoleById() method
        // when(roleService.getRoleById(1L)).thenReturn(role);

        // ResponseEntity<Result<Role>> response = roleController.get(1L);
        // assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
