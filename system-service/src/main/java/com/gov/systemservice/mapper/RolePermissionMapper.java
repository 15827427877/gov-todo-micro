package com.gov.systemservice.mapper;

import com.gov.systemservice.pojo.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色权限关联表Mapper
 *
 * @author chengbin
 * @since 2026-04-20
 */
@Mapper
public interface RolePermissionMapper {
    /**
     * 根据角色ID查询权限ID列表
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> selectPermissionIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限ID查询角色ID列表
     * @param permissionId 权限ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 插入角色权限关联
     * @param rolePermission 角色权限关联
     * @return 插入结果
     */
    int insert(RolePermission rolePermission);

    /**
     * 根据角色ID和权限ID删除关联
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 删除结果
     */
    int deleteByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 根据角色ID删除所有关联
     * @param roleId 角色ID
     * @return 删除结果
     */
    int deleteByRoleId(@Param("roleId") Long roleId);
}