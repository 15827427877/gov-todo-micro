package com.gov.systemservice.service.impl;

import com.gov.common.Result;
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
        try {
            if (!PasswordUtils.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("用户名或密码错误");
            }
        } catch (Exception e) {
            // 如果密码验证失败，检查用户输入的密码是否是默认密码
            if ("123456".equals(request.getPassword())) {
                // 如果是默认密码，重新加密并更新到数据库
                String encryptedPassword = PasswordUtils.encrypt(request.getPassword());
                user.setPassword(encryptedPassword);
                userMapper.update(user);
            } else {
                throw new RuntimeException("用户名或密码错误");
            }
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
        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtils.encrypt(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setDepartmentId(request.getDepartmentId());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setAvatar(""); // 默认为空字符串
        user.setStatus(1); // 默认为启用状态

        int result = userMapper.insert(user);
        if (result > 0) {
            LogUtils.info(UserServiceImpl.class, "用户注册成功: {}", request.getUsername());
            return true;
        }
        return false;
    }

    @Override
    public User selectByUsername(String username) {
        // 查找用户
        User user = userMapper.selectByUsername(username);
        return user;
    }

    /**
     * 密码重置
     *
     * @param request 密码重置请求
     * @return 重置结果
     */
    @Override
    public Result<String> resetPassword(ResetPasswordRequest request) {
        try {
            // 查找用户
            LogUtils.info(UserServiceImpl.class, "开始密码重置，用户名: {}", request.getUsername());
            User user = userMapper.selectByUsername(request.getUsername());
            if (user == null) {
                LogUtils.info(UserServiceImpl.class, "用户不存在: {}", request.getUsername());
                return Result.error("用户不存在");
            }

            // 验证旧密码
            if (!PasswordUtils.matches(request.getOldPassword(), user.getPassword())) {
                LogUtils.info(UserServiceImpl.class, "旧密码错误: {}", request.getUsername());
                return Result.error("旧密码错误");
            }

            // 更新密码
            user.setPassword(PasswordUtils.encrypt(request.getNewPassword()));
            LogUtils.info(UserServiceImpl.class, "更新密码，用户名: {}", request.getUsername());
            int result = userMapper.update(user);
            if (result > 0) {
                LogUtils.info(UserServiceImpl.class, "用户密码重置成功: {}", request.getUsername());
            } else {
                LogUtils.info(UserServiceImpl.class, "用户密码重置失败: {}", request.getUsername());
            }
            return Result.success("用户密码重置成功！");
        } catch (Exception e) {
            LogUtils.error(UserServiceImpl.class, "密码重置异常: {}", e.getMessage());
            return Result.error("密码重置失败: " + e.getMessage());
        }
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
