package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.pojo.UserNotificationSettings;
import com.gov.systemservice.pojo.UserLoginDevice;
import com.gov.systemservice.service.SystemSettingsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
public class SystemSettingsController {

    @Resource
    private SystemSettingsService systemSettingsService;

    @PutMapping("/user/profile")
    public Result<Void> updateProfile(@RequestBody UpdateProfileRequest request) {
        boolean success = systemSettingsService.updateProfile(1L, request.getName(), request.getEmail(), request.getPhone());
        return Result.success(null, "更新成功");
    }

    @PostMapping("/user/change-password")
    public Result<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            systemSettingsService.changePassword(1L, request.getOldPassword(), request.getNewPassword());
            return Result.success(null, "密码修改成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/settings/notification")
    public Result<UserNotificationSettings> getNotificationSettings() {
        UserNotificationSettings settings = systemSettingsService.getNotificationSettings(1L);
        return Result.success(settings);
    }

    @PutMapping("/settings/notification")
    public Result<Void> updateNotificationSettings(@RequestBody UserNotificationSettings settings) {
        boolean success = systemSettingsService.updateNotificationSettings(1L, settings);
        return Result.success(null, "更新成功");
    }

    @GetMapping("/user/login-devices")
    public Result<List<UserLoginDevice>> getLoginDevices() {
        List<UserLoginDevice> devices = systemSettingsService.getLoginDevices(1L);
        return Result.success(devices);
    }

    @DeleteMapping("/user/login-devices/{id}")
    public Result<Void> logoutDevice(@PathVariable Long id) {
        try {
            systemSettingsService.logoutDevice(id, 1L);
            return Result.success(null, "设备已下线");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/user/login-devices")
    public Result<Void> logoutAllDevices() {
        systemSettingsService.logoutAllDevices(1L);
        return Result.success(null, "已下线所有其他设备");
    }

    @GetMapping("/user/login-logs")
    public Result<Map<String, Object>> getLoginLogs(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> data = systemSettingsService.getLoginLogs(1L, page, size);
        return Result.success(data);
    }

    @GetMapping("/user/operation-logs")
    public Result<Map<String, Object>> getOperationLogs(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> data = systemSettingsService.getOperationLogs(1L, page, size);
        return Result.success(data);
    }

    public static class UpdateProfileRequest {
        private String name;
        private String email;
        private String phone;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}
