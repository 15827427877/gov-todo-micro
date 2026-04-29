package com.gov.todoservice.service.impl;

import com.gov.todoservice.mapper.ActivityMapper;
import com.gov.todoservice.pojo.Activity;
import com.gov.todoservice.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public List<Activity> getRecentActivities(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        List<Activity> activities = activityMapper.selectRecent(limit);
        return activities;
    }

    @Override
    public void recordActivity(Activity activity) {
        if (activity.getCreateTime() == null) {
            activity.setCreateTime(LocalDateTime.now());
        }
        activityMapper.insert(activity);
    }
}