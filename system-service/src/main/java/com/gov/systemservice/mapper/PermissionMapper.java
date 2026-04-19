package com.gov.systemservice.mapper;

import com.gov.systemservice.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper {
    List<Permission> selectAll();
    Permission selectById(@Param("id") Long id);
    int insert(Permission permission);
    int update(Permission permission);
    int delete(@Param("id") Long id);
    List<Permission> selectByRoleId(@Param("roleId") Long roleId);
}
