package com.gov.systemservice.mapper;

import com.gov.systemservice.pojo.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {

    List<Notification> selectByUserId(@Param("userId") Long userId);

    int updateReadById(@Param("id") Long id, @Param("userId") Long userId);

    int updateAllReadByUserId(@Param("userId") Long userId);

    int deleteById(@Param("id") Long id, @Param("userId") Long userId);

    Long countUnreadByUserId(@Param("userId") Long userId);

    int insert(Notification notification);
}
