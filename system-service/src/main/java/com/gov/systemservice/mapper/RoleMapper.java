package com.gov.systemservice.mapper;

import com.gov.systemservice.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表Mapper
 *
 * @author chengbin
 * @since 2026-04-20
 */
@Mapper
public interface RoleMapper {
    /**
     * 根据ID查询角色
     * @param id 角色ID
     * @return 角色信息
     */
    Role selectById(@Param("id") Long id);

    /**
     * 查询所有角色
     * @return 角色列表
     */
    List<Role> selectAll();

    /**
     * 插入角色
     * @param role 角色信息
     * @return 插入结果
     */
    int insert(Role role);

    /**
     * 更新角色
     * @param role 角色信息
     * @return 更新结果
     */
    int update(Role role);

    /**
     * 删除角色
     * @param id 角色ID
     * @return 删除结果
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据用户ID查询角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> selectByUserId(@Param("userId") Long userId);
}