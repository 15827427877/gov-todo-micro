package com.gov.systemservice.service;

import com.gov.systemservice.pojo.Notification;

import java.util.List;
import java.util.Map;

public interface NotificationService {

    Map<String, Object> getNotifications(Long userId, Integer page, Integer size);

    boolean markAsRead(Long id, Long userId);

    boolean markAllAsRead(Long userId);

    boolean deleteNotification(Long id, Long userId);

    Map<String, Object> getUnreadCount(Long userId);
}
