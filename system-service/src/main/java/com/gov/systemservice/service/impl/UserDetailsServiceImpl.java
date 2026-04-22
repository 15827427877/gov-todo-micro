package com.gov.systemservice.service.impl;

import com.gov.common.utils.PasswordUtils;
import com.gov.systemservice.mapper.UserMapper;
import com.gov.systemservice.pojo.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户详情服务实现类
 * 用于从数据库中获取用户信息并返回UserDetails对象
 *
 * @author chengbin
 * @since 2026-04-20
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中获取用户信息
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            // 如果用户不存在，创建一个默认的admin用户
            if ("admin".equals(username)) {
                user = new User();
                user.setUsername("admin");
                user.setPassword(PasswordUtils.encrypt("123456"));
                user.setRealName("管理员");
                user.setDepartmentId(1L);
                user.setPhone("");
                user.setEmail("");
                user.setAvatar("");
                user.setStatus(1);
                userMapper.insert(user);
            } else {
                throw new UsernameNotFoundException("用户不存在");
            }
        }

        // 构建UserDetails对象
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles() // 这里可以根据实际情况添加角色
                .build();
    }
}