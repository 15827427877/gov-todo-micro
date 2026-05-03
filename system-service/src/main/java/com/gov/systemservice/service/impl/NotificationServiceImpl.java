package com.gov.systemservice.service.impl;

import com.gov.systemservice.mapper.NotificationMapper;
import com.gov.systemservice.pojo.Notification;
import com.gov.systemservice.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Resource
    private NotificationMapper notificationMapper;

    @Override
    public Map<String, Object> getNotifications(Long userId, Integer page, Integer size) {
        List<Notification> list = notificationMapper.selectByUserId(userId);
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
    @Transactional
    public boolean markAsRead(Long id, Long userId) {
        return notificationMapper.updateReadById(id, userId) > 0;
    }

    @Override
    @Transactional
    public boolean markAllAsRead(Long userId) {
        notificationMapper.updateAllReadByUserId(userId);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteNotification(Long id, Long userId) {
        return notificationMapper.deleteById(id, userId) > 0;
    }

    @Override
    public Map<String, Object> getUnreadCount(Long userId) {
        Long count = notificationMapper.countUnreadByUserId(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("unreadCount", count);
        return result;
    }
}
