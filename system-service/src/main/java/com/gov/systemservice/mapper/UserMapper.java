package com.gov.systemservice.mapper;

import com.gov.systemservice.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User selectByUsername(@Param("username") String username);
    User selectById(@Param("id") Long id);
    int insert(User user);
    int update(User user);
    int updateLastLoginInfo(@Param("id") Long id, @Param("lastLoginTime") java.util.Date lastLoginTime, @Param("loginIp") String loginIp);
}
