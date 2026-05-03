package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    @GetMapping
    public Result<Map<String, Object>> getNotifications(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Map<String, Object> data = notificationService.getNotifications(1L, page, size);
        return Result.success(data);
    }

    @PutMapping("/{id}")
    public Result<Void> markAsRead(@PathVariable Long id) {
        boolean success = notificationService.markAsRead(id, 1L);
        return Result.success(null, "标记成功");
    }

    @PutMapping("/read-all")
    public Result<Void> markAllAsRead() {
        notificationService.markAllAsRead(1L);
        return Result.success(null, "全部已读");
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteNotification(@PathVariable Long id) {
        boolean success = notificationService.deleteNotification(id, 1L);
        return Result.success(null, "删除成功");
    }

    @GetMapping("/count")
    public Result<Map<String, Object>> getUnreadCount() {
        Map<String, Object> data = notificationService.getUnreadCount(1L);
        return Result.success(data);
    }
}
