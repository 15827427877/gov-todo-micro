package com.gov.todoservice.controller;

import com.gov.common.Result;
import com.gov.todoservice.pojo.TodoItem;
import com.gov.todoservice.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/list")
    public Result<?> list(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "sortField", defaultValue = "") String sortField,
            @RequestParam(value = "sortOrder", defaultValue = "") String sortOrder) {
        // 这里需要实现分页、排序、搜索逻辑
        // 暂时返回模拟数据，后续需要实现
        List<TodoItem> todos = todoService.getAllTodos();
        return Result.success(java.util.Map.of(
                "list", todos,
                "total", todos.size()
        ), "操作成功");
    }

    @GetMapping("/{id}")
    public Result<TodoItem> getById(@PathVariable Long id) {
        TodoItem todo = todoService.getTodoById(id);
        if (todo == null) {
            return Result.error("Todo not found");
        }
        return Result.success(todo, "操作成功");
    }

    @PostMapping
    public Result<TodoItem> create(@RequestBody TodoItem todoItem) {
        if (todoItem.getTitle() == null || todoItem.getTitle().trim().isEmpty()) {
            return Result.error("Title cannot be empty");
        }
        // 默认状态为待处理
        if (todoItem.getStatus() == null || todoItem.getStatus().trim().isEmpty()) {
            todoItem.setStatus("待处理");
        }
        // 格式化截止日期为 YYYY-MM-DD 格式
        if (todoItem.getDeadline() != null && !todoItem.getDeadline().trim().isEmpty()) {
            try {
                // 解析日期字符串，无论输入格式如何，都转换为 YYYY-MM-DD 格式
                java.time.LocalDate date = java.time.LocalDate.parse(todoItem.getDeadline().substring(0, 10));
                todoItem.setDeadline(date.toString());
            } catch (Exception e) {
                // 如果解析失败，保持原格式
            }
        }
        TodoItem created = todoService.createTodo(todoItem);
        return Result.success(created, "新增成功");
    }

    @PutMapping("/{id}")
    public Result<TodoItem> update(@PathVariable Long id, @RequestBody TodoItem todoItem) {
        try {
            // 格式化截止日期为 YYYY-MM-DD 格式
            if (todoItem.getDeadline() != null && !todoItem.getDeadline().trim().isEmpty()) {
                try {
                    // 解析日期字符串，无论输入格式如何，都转换为 YYYY-MM-DD 格式
                    java.time.LocalDate date = java.time.LocalDate.parse(todoItem.getDeadline().substring(0, 10));
                    todoItem.setDeadline(date.toString());
                } catch (Exception e) {
                    // 如果解析失败，保持原格式
                }
            }
            TodoItem updated = todoService.updateTodo(id, todoItem);
            return Result.success(updated, "更新成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return Result.success(null, "删除成功");
    }

    @DeleteMapping("/batch")
    public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
        boolean deleted = todoService.deleteTodos(ids);
        return Result.success(deleted);
    }

    /**
     * 修改状态
     */
    @PatchMapping("/{id}/status")
    public Result<TodoItem> updateStatus(@PathVariable Long id, @RequestBody java.util.Map<String, String> status) {
        String statusValue = status.get("status");
        TodoItem updatedTodo = todoService.updateStatus(id, statusValue);
        return Result.success(updatedTodo, "状态更新成功");
    }

    /**
     * 转交待办
     */
    @PatchMapping("/{id}/transfer")
    public Result<TodoItem> transferTodo(@PathVariable Long id, @RequestBody java.util.Map<String, String> assignee) {
        String newAssignee = assignee.get("assignee");
        TodoItem updatedTodo = todoService.transferTodo(id, newAssignee);
        return Result.success(updatedTodo, "转交成功");
    }

    /**
     * 待办统计
     */
    @GetMapping("/statistics")
    public Result<java.util.Map<String, Object>> statistics(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        java.util.Map<String, Object> statistics = todoService.getStatistics();
        return Result.success(statistics, "操作成功");
    }
}
