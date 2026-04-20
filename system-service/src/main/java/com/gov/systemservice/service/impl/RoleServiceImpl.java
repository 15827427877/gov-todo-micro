package com.gov.systemservice.service.impl;

import com.gov.common.utils.LogUtils;
import com.gov.systemservice.mapper.RoleMapper;
import com.gov.systemservice.mapper.UserRoleMapper;
import com.gov.systemservice.pojo.Role;
import com.gov.systemservice.pojo.UserRole;
import com.gov.systemservice.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Role> getAllRoles() {
        return roleMapper.selectAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    public boolean createRole(Role role) {
        int result = roleMapper.insert(role);
        if (result > 0) {
            LogUtils.info(RoleServiceImpl.class, "创建角色成功: {}", role.getRoleName());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateRole(Role role) {
        int result = roleMapper.update(role);
        if (result > 0) {
            LogUtils.info(RoleServiceImpl.class, "更新角色成功: {}", role.getRoleName());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteRole(Long id) {
        int result = roleMapper.deleteById(id);
        if (result > 0) {
            LogUtils.info(RoleServiceImpl.class, "删除角色成功: {}", id);
            return true;
        }
        return false;
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        return roleMapper.selectByUserId(userId);
    }

    @Override
    @Transactional
    public boolean assignRolesToUser(Long userId, List<Long> roleIds) {
        // 先删除用户原有的角色
        userRoleMapper.deleteByUserId(userId);

        // 为用户分配新的角色
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }

        LogUtils.info(RoleServiceImpl.class, "为用户分配角色成功: userId={}, roleIds={}", userId, roleIds);
        return true;
    }
}
