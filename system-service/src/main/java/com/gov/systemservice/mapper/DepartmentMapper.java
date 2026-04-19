package com.gov.systemservice.mapper;

import com.gov.systemservice.pojo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    List<Department> selectAll();
    Department selectById(@Param("id") Long id);
    int insert(Department department);
    int update(Department department);
    int delete(@Param("id") Long id);
    List<Department> selectByParentId(@Param("parentId") Long parentId);
}
