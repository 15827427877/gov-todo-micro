package com.gov.systemservice.mapper;

import com.gov.systemservice.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限表Mapper
 *
 * @author chengbin
 * @since 2026-04-20
 */
@Mapper
public interface PermissionMapper {
    /**
     * 根据ID查询权限
     * @param id 权限ID
     * @return 权限信息
     */
    Permission selectById(@Param("id") Long id);

    /**
     * 查询所有权限
     * @return 权限列表
     */
    List<Permission> selectAll();

    /**
     * 插入权限
     * @param permission 权限信息
     * @return 插入结果
     */
    int insert(Permission permission);

    /**
     * 更新权限
     * @param permission 权限信息
     * @return 更新结果
     */
    int update(Permission permission);

    /**
     * 删除权限
     * @param id 权限ID
     * @return 删除结果
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据角色ID查询权限列表
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> selectByRoleId(@Param("roleId") Long roleId);
}