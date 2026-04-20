package com.gov.systemservice.mapper;

import com.gov.systemservice.pojo.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联表Mapper
 *
 * @author chengbin
 * @since 2026-04-20
 */
@Mapper
public interface UserRoleMapper {
    /**
     * 根据用户ID查询角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询用户ID列表
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    List<Long> selectUserIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 插入用户角色关联
     * @param userRole 用户角色关联
     * @return 插入结果
     */
    int insert(UserRole userRole);

    /**
     * 根据用户ID和角色ID删除关联
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 删除结果
     */
    int deleteByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 根据用户ID删除所有关联
     * @param userId 用户ID
     * @return 删除结果
     */
    int deleteByUserId(@Param("userId") Long userId);
}