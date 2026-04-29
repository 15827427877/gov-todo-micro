package com.gov.todoservice.controller;

import com.gov.common.Result;
import com.gov.todoservice.pojo.Activity;
import com.gov.todoservice.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/recent")
    public Result<List<Activity>> getRecentActivities(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<Activity> activities = activityService.getRecentActivities(limit);
        return Result.success(activities, "操作成功");
    }

    @PostMapping("/record")
    public Result<Boolean> recordActivity(@RequestBody Activity activity) {
        activityService.recordActivity(activity);
        return Result.success(true, "记录成功");
    }
}