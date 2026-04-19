package com.gov.systemservice.mapper;

import com.gov.systemservice.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    List<Role> selectAll();
    Role selectById(@Param("id") Long id);
    int insert(Role role);
    int update(Role role);
    int delete(@Param("id") Long id);
    List<Role> selectByUserId(@Param("userId") Long userId);
}
