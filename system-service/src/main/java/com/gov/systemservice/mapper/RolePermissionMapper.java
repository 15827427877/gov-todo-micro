package com.gov.systemservice.mapper;

import com.gov.systemservice.pojo.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RolePermissionMapper {
    List<RolePermission> selectByRoleId(@Param("roleId") Long roleId);
    int insert(RolePermission rolePermission);
    int deleteByRoleId(@Param("roleId") Long roleId);
}
