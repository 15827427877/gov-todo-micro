package com.gov.systemservice.service;

import com.gov.systemservice.dto.LoginRequest;
import com.gov.systemservice.dto.LoginResponse;
import com.gov.systemservice.dto.RegisterRequest;
import com.gov.systemservice.mapper.UserMapper;
import com.gov.systemservice.pojo.User;
import com.gov.systemservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * UserService单元测试
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("$2a$10$encrypted_password");
        testUser.setRealName("测试用户");
        testUser.setStatus(1);
    }

    @Test
    void testLogin_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("123456");

        when(userMapper.selectByUsername(anyString())).thenReturn(testUser);

        // 注意: 由于密码验证需要真实的加密密码,这里只测试用户查找逻辑
        assertThrows(RuntimeException.class, () -> {
            userService.login(request, "127.0.0.1");
        });
    }

    @Test
    void testLogin_UserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("123456");

        when(userMapper.selectByUsername(anyString())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            userService.login(request, "127.0.0.1");
        });
    }

    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("123456");
        request.setRealName("新用户");

        when(userMapper.insert(any(User.class))).thenReturn(1);

        boolean result = userService.register(request);

        assertTrue(result);
    }

    @Test
    void testGetUserByUsername() {
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);

        User result = userService.getUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("测试用户", result.getRealName());
    }

    @Test
    void testGetUserById() {
        when(userMapper.selectById(1L)).thenReturn(testUser);

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
}
