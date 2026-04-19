package com.gov.systemservice.service.impl;

import com.gov.common.utils.JwtUtils;
import com.gov.common.utils.LogUtils;
import com.gov.common.utils.PasswordUtils;
import com.gov.systemservice.dto.LoginRequest;
import com.gov.systemservice.dto.LoginResponse;
import com.gov.systemservice.dto.RegisterRequest;
import com.gov.systemservice.dto.ResetPasswordRequest;
import com.gov.systemservice.mapper.UserMapper;
import com.gov.systemservice.pojo.User;
import com.gov.systemservice.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现类
 * 实现用户登录、注册、密码重置等业务逻辑
 * 
 * @author chengbin
 * @since 2026-04-19
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param request 登录请求
     * @param ip 客户端IP地址
     * @return 登录响应
     */
    @Override
    public LoginResponse login(LoginRequest request, String ip) {
        // 查找用户
        User user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用");
        }

        // 验证密码
        if (!PasswordUtils.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 更新登录信息
        userMapper.updateLastLoginInfo(user.getId(), new Date(), ip);

        // 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("realName", user.getRealName());
        String token = JwtUtils.generateToken(user.getUsername(), claims);

        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRealName(user.getRealName());
        userInfo.setDepartmentId(user.getDepartmentId());
        userInfo.setPhone(user.getPhone());
        userInfo.setEmail(user.getEmail());
        userInfo.setStatus(user.getStatus());

        response.setUserInfo(userInfo);

        LogUtils.info(UserServiceImpl.class, "用户登录成功: {}", user.getUsername());
        return response;
    }

    /**
     * 用户注册
     * @param request 注册请求
     * @return 注册结果
     */
    @Override
    public boolean register(RegisterRequest request) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(request.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtils.encrypt(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setDepartmentId(request.getDepartmentId());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setStatus(1); // 默认为启用状态

        int result = userMapper.insert(user);
        if (result > 0) {
            LogUtils.info(UserServiceImpl.class, "用户注册成功: {}", request.getUsername());
            return true;
        }
        return false;
    }

    /**
     * 密码重置
     * @param request 密码重置请求
     * @return 重置结果
     */
    @Override
    public boolean resetPassword(ResetPasswordRequest request) {
        // 查找用户
        User user = userMapper.selectByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        if (!PasswordUtils.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        // 更新密码
        user.setPassword(PasswordUtils.encrypt(request.getNewPassword()));
        int result = userMapper.update(user);
        if (result > 0) {
            LogUtils.info(UserServiceImpl.class, "用户密码重置成功: {}", request.getUsername());
            return true;
        }
        return false;
    }

    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
}
