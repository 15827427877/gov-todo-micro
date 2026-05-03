package com.gov.systemservice.mapper;

import com.gov.systemservice.pojo.UserNotificationSettings;
import com.gov.systemservice.pojo.UserLoginDevice;
import com.gov.systemservice.pojo.LoginLog;
import com.gov.systemservice.pojo.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemSettingsMapper {

    UserNotificationSettings selectNotificationSettingsByUserId(@Param("userId") Long userId);

    int insertNotificationSettings(UserNotificationSettings settings);

    int updateNotificationSettings(UserNotificationSettings settings);

    List<UserLoginDevice> selectLoginDevicesByUserId(@Param("userId") Long userId);

    int deleteLoginDeviceById(@Param("id") Long id);

    int deleteLoginDevicesByUserIdExceptCurrent(@Param("userId") Long userId);

    List<LoginLog> selectLoginLogsByUserId(@Param("userId") Long userId);

    List<OperationLog> selectOperationLogsByUserId(@Param("userId") Long userId);
}
