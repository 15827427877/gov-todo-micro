package com.gov.systemservice.service.impl;

import com.gov.common.utils.PasswordUtils;
import com.gov.systemservice.mapper.SystemSettingsMapper;
import com.gov.systemservice.mapper.UserMapper;
import com.gov.systemservice.pojo.User;
import com.gov.systemservice.pojo.UserNotificationSettings;
import com.gov.systemservice.pojo.UserLoginDevice;
import com.gov.systemservice.pojo.LoginLog;
import com.gov.systemservice.pojo.OperationLog;
import com.gov.systemservice.service.SystemSettingsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemSettingsServiceImpl implements SystemSettingsService {

    @Resource
    private SystemSettingsMapper systemSettingsMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public UserNotificationSettings getNotificationSettings(Long userId) {
        UserNotificationSettings settings = systemSettingsMapper.selectNotificationSettingsByUserId(userId);
        if (settings == null) {
            settings = new UserNotificationSettings();
            settings.setUserId(userId);
            settings.setTodoReminder(true);
            settings.setReminderTime("09:00");
            settings.setApprovalNotice(true);
            settings.setSystemNotice(true);
            settings.setNotifyWaysStr("站内信");
            systemSettingsMapper.insertNotificationSettings(settings);
        }
        settings.setNotifyWaysStr(settings.getNotifyWaysStr());
        return settings;
    }

    @Override
    @Transactional
    public boolean updateNotificationSettings(Long userId, UserNotificationSettings settings) {
        settings.setUserId(userId);
        if (settings.getNotifyWays() != null) {
            settings.setNotifyWaysStr(String.join(",", settings.getNotifyWays()));
        }
        UserNotificationSettings existing = systemSettingsMapper.selectNotificationSettingsByUserId(userId);
        if (existing == null) {
            return systemSettingsMapper.insertNotificationSettings(settings) > 0;
        }
        return systemSettingsMapper.updateNotificationSettings(settings) > 0;
    }

    @Override
    @Transactional
    public boolean updateProfile(Long userId, String name, String email, String phone) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        if (name != null) {
            user.setRealName(name);
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (phone != null) {
            user.setPhone(phone);
        }
        return userMapper.update(user) > 0;
    }

    @Override
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!PasswordUtils.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        user.setPassword(PasswordUtils.encrypt(newPassword));
        return userMapper.update(user) > 0;
    }

    @Override
    public List<UserLoginDevice> getLoginDevices(Long userId) {
        return systemSettingsMapper.selectLoginDevicesByUserId(userId);
    }

    @Override
    @Transactional
    public boolean logoutDevice(Long id, Long userId) {
        UserLoginDevice device = systemSettingsMapper.selectLoginDevicesByUserId(userId).stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (device == null) {
            throw new RuntimeException("设备不存在");
        }
        return systemSettingsMapper.deleteLoginDeviceById(id) > 0;
    }

    @Override
    @Transactional
    public boolean logoutAllDevices(Long userId) {
        return systemSettingsMapper.deleteLoginDevicesByUserIdExceptCurrent(userId) > 0;
    }

    @Override
    public Map<String, Object> getLoginLogs(Long userId, Integer page, Integer size) {
        List<LoginLog> list = systemSettingsMapper.selectLoginLogsByUserId(userId);
        int total = list.size();

        int offset = (page - 1) * size;
        int end = Math.min(offset + size, total);
        if (offset >= total) {
            list = List.of();
        } else {
            list = list.subList(offset, end);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);

        return result;
    }

    @Override
    public Map<String, Object> getOperationLogs(Long userId, Integer page, Integer size) {
        List<OperationLog> list = systemSettingsMapper.selectOperationLogsByUserId(userId);
        int total = list.size();

        int offset = (page - 1) * size;
        int end = Math.min(offset + size, total);
        if (offset >= total) {
            list = List.of();
        } else {
            list = list.subList(offset, end);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);

        return result;
    }
}
