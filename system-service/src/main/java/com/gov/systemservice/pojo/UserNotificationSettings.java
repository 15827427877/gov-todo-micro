package com.gov.systemservice.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserNotificationSettings {
    private Long id;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("todoReminder")
    private Boolean todoReminder;

    @JsonProperty("reminderTime")
    private String reminderTime;

    @JsonProperty("approvalNotice")
    private Boolean approvalNotice;

    @JsonProperty("systemNotice")
    private Boolean systemNotice;

    @JsonProperty("notifyWays")
    private List<String> notifyWays;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getTodoReminder() {
        return todoReminder;
    }

    public void setTodoReminder(Boolean todoReminder) {
        this.todoReminder = todoReminder;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public Boolean getApprovalNotice() {
        return approvalNotice;
    }

    public void setApprovalNotice(Boolean approvalNotice) {
        this.approvalNotice = approvalNotice;
    }

    public Boolean getSystemNotice() {
        return systemNotice;
    }

    public void setSystemNotice(Boolean systemNotice) {
        this.systemNotice = systemNotice;
    }

    public List<String> getNotifyWays() {
        return notifyWays;
    }

    public void setNotifyWays(List<String> notifyWays) {
        this.notifyWays = notifyWays;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getNotifyWaysStr() {
        if (notifyWays == null || notifyWays.isEmpty()) {
            return "";
        }
        return String.join(",", notifyWays);
    }

    public void setNotifyWaysStr(String notifyWaysStr) {
        if (notifyWaysStr == null || notifyWaysStr.isEmpty()) {
            this.notifyWays = Arrays.asList();
        } else {
            this.notifyWays = Arrays.stream(notifyWaysStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
    }
}
