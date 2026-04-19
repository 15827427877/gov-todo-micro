package com.gov.systemservice.controller;

import com.gov.systemservice.dto.LoginRequest;
import com.gov.systemservice.dto.RegisterRequest;
import com.gov.systemservice.dto.ResetPasswordRequest;
import com.gov.systemservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    public void testLogin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("123456");

        // Mock userService.login() method
        // when(userService.login(request, "127.0.0.1")).thenReturn(new LoginResponse());

        // ResponseEntity<Result<LoginResponse>> response = userController.login(request, null);
        // assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");
        request.setPassword("123456");
        request.setRealName("测试用户");
        request.setDepartmentId(1L);
        request.setPhone("13800138000");
        request.setEmail("test@example.com");

        // Mock userService.register() method
        // when(userService.register(request)).thenReturn(true);

        // ResponseEntity<Result<Boolean>> response = userController.register(request);
        // assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testResetPassword() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setUsername("admin");
        request.setOldPassword("123456");
        request.setNewPassword("654321");

        // Mock userService.resetPassword() method
        // when(userService.resetPassword(request)).thenReturn(true);

        // ResponseEntity<Result<Boolean>> response = userController.resetPassword(request);
        // assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
