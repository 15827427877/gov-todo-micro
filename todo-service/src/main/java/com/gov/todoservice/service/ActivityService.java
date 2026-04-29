package com.gov.todoservice.service;

import com.gov.todoservice.pojo.Activity;

import java.util.List;

public interface ActivityService {
    List<Activity> getRecentActivities(Integer limit);

    void recordActivity(Activity activity);
}