package com.gov.todoservice.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Activity {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String content;
    private String actionType;
    private String status;
    private String statusType;
    private String icon;
    private Long relatedId;
    private String relatedType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    public String getTimeAgo() {
        if (createTime == null) {
            return "未知";
        }
        LocalDateTime now = LocalDateTime.now();
        long seconds = java.time.Duration.between(createTime, now).getSeconds();

        if (seconds < 60) {
            return "刚刚";
        } else if (seconds < 3600) {
            return (seconds / 60) + "分钟前";
        } else if (seconds < 86400) {
            return (seconds / 3600) + "小时前";
        } else {
            return (seconds / 86400) + "天前";
        }
    }
}