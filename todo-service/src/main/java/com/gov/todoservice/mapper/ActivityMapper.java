package com.gov.todoservice.mapper;

import com.gov.todoservice.pojo.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivityMapper {
    List<Activity> selectRecentByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    int insert(Activity activity);

    List<Activity> selectRecent(@Param("limit") Integer limit);
}