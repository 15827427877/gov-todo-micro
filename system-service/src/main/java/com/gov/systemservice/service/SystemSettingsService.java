package com.gov.systemservice.service;

import com.gov.systemservice.pojo.UserNotificationSettings;
import com.gov.systemservice.pojo.UserLoginDevice;
import com.gov.systemservice.pojo.LoginLog;
import com.gov.systemservice.pojo.OperationLog;

import java.util.List;
import java.util.Map;

public interface SystemSettingsService {

    UserNotificationSettings getNotificationSettings(Long userId);

    boolean updateNotificationSettings(Long userId, UserNotificationSettings settings);

    boolean updateProfile(Long userId, String name, String email, String phone);

    boolean changePassword(Long userId, String oldPassword, String newPassword);

    List<UserLoginDevice> getLoginDevices(Long userId);

    boolean logoutDevice(Long id, Long userId);

    boolean logoutAllDevices(Long userId);

    Map<String, Object> getLoginLogs(Long userId, Integer page, Integer size);

    Map<String, Object> getOperationLogs(Long userId, Integer page, Integer size);
}
