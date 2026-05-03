package com.gov.todoservice.service.impl;

import com.gov.todoservice.mapper.TodoMapper;
import com.gov.todoservice.pojo.Activity;
import com.gov.todoservice.pojo.TodoItem;
import com.gov.todoservice.service.ActivityService;
import com.gov.todoservice.service.TodoService;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoMapper todoMapper;

    @Autowired
    private ActivityService activityService;

    @Override
    @Cacheable(value = "todos", key = "'all'")
    @SentinelResource(value = "getAllTodos", blockHandler = "getAllTodosBlockHandler")
    public List<TodoItem> getAllTodos() {
        return todoMapper.selectAll();
    }

    @Override
    @Cacheable(value = "todos", key = "#id", unless = "#result == null")
    @SentinelResource(value = "getTodoById", blockHandler = "getTodoByIdBlockHandler")
    public TodoItem getTodoById(Long id) {
        return todoMapper.selectById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "todos", allEntries = true)
    @SentinelResource(value = "createTodo", blockHandler = "createTodoBlockHandler")
    public TodoItem createTodo(TodoItem todoItem) {
        todoItem.setCreateTime(LocalDateTime.now());
        todoItem.setUpdateTime(LocalDateTime.now());
        todoItem.setCompleted(false);
        todoMapper.insert(todoItem);

        // 记录活动
        Activity activity = new Activity();
        activity.setUserId(todoItem.getUserId() != null ? todoItem.getUserId() : 1L);
        activity.setUserName(todoItem.getAssignee() != null ? todoItem.getAssignee() : "system");
        activity.setTitle("创建了待办事项");
        activity.setContent("创建了待办事项：" + todoItem.getTitle());
        activity.setActionType("CREATE");
        activity.setStatus("成功");
        activity.setStatusType("success");
        activity.setIcon("el-icon-plus");
        activity.setRelatedId(todoItem.getId());
        activity.setRelatedType("TODO");
        activityService.recordActivity(activity);

        return todoItem;
    }

    @Override
    @Transactional
    @CacheEvict(value = "todos", allEntries = true)
    public TodoItem updateTodo(Long id, TodoItem todoItem) {
        TodoItem existingTodo = todoMapper.selectById(id);
        if (existingTodo == null) {
            throw new RuntimeException("Todo not found with id: " + id);
        }

        if (todoItem.getTitle() != null) {
            existingTodo.setTitle(todoItem.getTitle());
        }
        if (todoItem.getAssignee() != null) {
            existingTodo.setAssignee(todoItem.getAssignee());
        }
        if (todoItem.getStatus() != null) {
            existingTodo.setStatus(todoItem.getStatus());
        }
        if (todoItem.getDeadline() != null) {
            existingTodo.setDeadline(todoItem.getDeadline());
        }
        if (todoItem.getDescription() != null) {
            existingTodo.setDescription(todoItem.getDescription());
        }
        if (todoItem.getCompleted() != null) {
            existingTodo.setCompleted(todoItem.getCompleted());
        }
        existingTodo.setUpdateTime(LocalDateTime.now());

        todoMapper.update(existingTodo);

        // 记录活动
        Activity activity = new Activity();
        activity.setUserId(existingTodo.getUserId() != null ? existingTodo.getUserId() : 1L);
        activity.setUserName(existingTodo.getAssignee() != null ? existingTodo.getAssignee() : "system");
        activity.setTitle("更新了待办事项");
        activity.setContent("更新了待办事项：" + existingTodo.getTitle());
        activity.setActionType("UPDATE");
        activity.setStatus("成功");
        activity.setStatusType("primary");
        activity.setIcon("el-icon-edit");
        activity.setRelatedId(existingTodo.getId());
        activity.setRelatedType("TODO");
        activityService.recordActivity(activity);

        return existingTodo;
    }

    @Override
    @Transactional
    @CacheEvict(value = "todos", allEntries = true)
    public boolean deleteTodo(Long id) {
        return todoMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    @CacheEvict(value = "todos", allEntries = true)
    public boolean deleteTodos(List<Long> ids) {
        return todoMapper.deleteByIds(ids) > 0;
    }

    @Override
    @Transactional
    @CacheEvict(value = "todos", allEntries = true)
    public TodoItem updateStatus(Long id, String status) {
        TodoItem existingTodo = todoMapper.selectById(id);
        if (existingTodo == null) {
            throw new RuntimeException("Todo not found with id: " + id);
        }
        existingTodo.setStatus(status);
        existingTodo.setCompleted("已完成".equals(status));
        existingTodo.setUpdateTime(java.time.LocalDateTime.now());
        todoMapper.update(existingTodo);

        // 记录活动
        Activity activity = new Activity();
        activity.setUserId(existingTodo.getUserId() != null ? existingTodo.getUserId() : 1L);
        activity.setUserName(existingTodo.getAssignee() != null ? existingTodo.getAssignee() : "system");
        activity.setTitle("更新了待办状态");
        activity.setContent("将待办事项\"" + existingTodo.getTitle() + "\"状态更新为\"" + status + "\"");
        activity.setActionType("STATUS_CHANGE");
        activity.setStatus(status);
        activity.setStatusType("已完成".equals(status) ? "success" : "primary");
        activity.setIcon("el-icon-edit");
        activity.setRelatedId(existingTodo.getId());
        activity.setRelatedType("TODO");
        activityService.recordActivity(activity);

        return existingTodo;
    }

    @Override
    @Transactional
    @CacheEvict(value = "todos", allEntries = true)
    public TodoItem transferTodo(Long id, String assignee) {
        TodoItem existingTodo = todoMapper.selectById(id);
        if (existingTodo == null) {
            throw new RuntimeException("Todo not found with id: " + id);
        }
        existingTodo.setAssignee(assignee);
        existingTodo.setUpdateTime(java.time.LocalDateTime.now());
        todoMapper.update(existingTodo);

        // 记录活动
        Activity activity = new Activity();
        activity.setUserId(existingTodo.getUserId() != null ? existingTodo.getUserId() : 1L);
        activity.setUserName(existingTodo.getAssignee() != null ? existingTodo.getAssignee() : "system");
        activity.setTitle("转交待办事项");
        activity.setContent("将待办事项\"" + existingTodo.getTitle() + "\"转交给\"" + assignee + "\"");
        activity.setActionType("TRANSFER");
        activity.setStatus("成功");
        activity.setStatusType("info");
        activity.setIcon("el-icon-s-promotion");
        activity.setRelatedId(existingTodo.getId());
        activity.setRelatedType("TODO");
        activityService.recordActivity(activity);

        return existingTodo;
    }

    @Override
    public Map<String, Object> getStatistics() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate sevenDaysAgo = today.minusDays(7);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<TodoItem> allTodos = todoMapper.selectAll();
        List<TodoItem> todayTodos = todoMapper.selectByCreateDate(today.format(formatter));
        List<TodoItem> yesterdayTodos = todoMapper.selectByCreateDate(yesterday.format(formatter));
        List<TodoItem> sevenDaysTodos = todoMapper.selectByCreateDateBetween(sevenDaysAgo.format(formatter), today.format(formatter));

        long total = allTodos.size();
        long todayAdded = todayTodos.size();
        long yesterdayAdded = yesterdayTodos.size();
        long completed = allTodos.stream().filter(todo -> todo.getCompleted() != null && todo.getCompleted()).count();
        long pending = total - completed;
        long pendingApproval = todoMapper.countByStatus("待审批");

        double completionRate = total > 0 ? (double) completed / total * 100 : 0.0;

        double totalTrend = yesterdayAdded > 0 ? (double) (todayAdded - yesterdayAdded) / yesterdayAdded * 100 : 0.0;
        double addedTrend = yesterdayAdded > 0 ? (double) (todayAdded - yesterdayAdded) / yesterdayAdded * 100 : 0.0;

        double completedYesterday = yesterdayTodos.stream().filter(todo -> todo.getCompleted() != null && todo.getCompleted()).count();
        double completionRateYesterday = yesterdayTodos.size() > 0 ? completedYesterday / yesterdayTodos.size() * 100 : 0.0;
        double rateTrend = completionRate - completionRateYesterday;

        double approvalTrend = 0.0;

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", total);
        statistics.put("todayAdded", todayAdded);
        statistics.put("completionRate", Math.round(completionRate * 100.0) / 100.0);
        statistics.put("pendingApproval", pendingApproval);
        statistics.put("totalTrend", Math.round(totalTrend * 100.0) / 100.0);
        statistics.put("addedTrend", Math.round(addedTrend * 100.0) / 100.0);
        statistics.put("rateTrend", Math.round(rateTrend * 100.0) / 100.0);
        statistics.put("approvalTrend", approvalTrend);

        return statistics;
    }

    public List<TodoItem> getAllTodosBlockHandler(com.alibaba.csp.sentinel.slots.block.BlockException ex) {
        throw new RuntimeException("获取待办列表请求过于频繁，请稍后重试");
    }

    public TodoItem getTodoByIdBlockHandler(Long id, com.alibaba.csp.sentinel.slots.block.BlockException ex) {
        throw new RuntimeException("获取待办详情请求过于频繁，请稍后重试");
    }

    public TodoItem createTodoBlockHandler(TodoItem todoItem, com.alibaba.csp.sentinel.slots.block.BlockException ex) {
        throw new RuntimeException("创建待办事项请求过于频繁，请稍后重试");
    }
}