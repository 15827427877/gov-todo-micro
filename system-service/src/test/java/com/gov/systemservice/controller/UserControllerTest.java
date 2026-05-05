package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.dto.LoginRequest;
import com.gov.systemservice.dto.LoginResponse;
import com.gov.systemservice.dto.RegisterRequest;
import com.gov.systemservice.dto.ResetPasswordRequest;
import com.gov.systemservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
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

        LoginResponse response = new LoginResponse();
        response.setToken("test-token");
        response.setName("管理员");
        response.setUserId("1");

        when(userService.login(any(LoginRequest.class), anyString())).thenReturn(response);

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        when(httpRequest.getHeader("X-Forwarded-For")).thenReturn("127.0.0.1");

        Result<LoginResponse> result = userController.login(request, httpRequest);
        
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("test-token", result.getData().getToken());
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

        when(userService.register(any(RegisterRequest.class))).thenReturn(true);

        Result<Boolean> result = userController.register(request);
        
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertTrue(result.getData());
    }

    @Test
    public void testResetPassword() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setUsername("admin");
        request.setOldPassword("123456");
        request.setNewPassword("654321");

        when(userService.resetPassword(any(ResetPasswordRequest.class)))
            .thenReturn(Result.success("密码重置成功"));

        Result<String> result = userController.resetPassword(request);
        
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("密码重置成功", result.getData());
    }
}
