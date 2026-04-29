package com.gov.todoservice.controller;

import com.gov.common.Result;
import com.gov.todoservice.pojo.TodoItem;
import com.gov.todoservice.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        List<TodoItem> todos = todoService.getAllTodos();
        return Result.success(Map.of(
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
        if (todoItem.getStatus() == null || todoItem.getStatus().trim().isEmpty()) {
            todoItem.setStatus("待处理");
        }
        if (todoItem.getDeadline() != null && !todoItem.getDeadline().trim().isEmpty()) {
            try {
                java.time.LocalDate date = java.time.LocalDate.parse(todoItem.getDeadline().substring(0, 10));
                todoItem.setDeadline(date.toString());
            } catch (Exception e) {
            }
        }
        TodoItem created = todoService.createTodo(todoItem);
        return Result.success(created, "新增成功");
    }

    @PutMapping("/{id}")
    public Result<TodoItem> update(@PathVariable Long id, @RequestBody TodoItem todoItem) {
        try {
            if (todoItem.getDeadline() != null && !todoItem.getDeadline().trim().isEmpty()) {
                try {
                    java.time.LocalDate date = java.time.LocalDate.parse(todoItem.getDeadline().substring(0, 10));
                    todoItem.setDeadline(date.toString());
                } catch (Exception e) {
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

    @PatchMapping("/{id}/status")
    public Result<TodoItem> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> status) {
        String statusValue = status.get("status");
        TodoItem updatedTodo = todoService.updateStatus(id, statusValue);
        return Result.success(updatedTodo, "状态更新成功");
    }

    @PatchMapping("/{id}/transfer")
    public Result<TodoItem> transferTodo(@PathVariable Long id, @RequestBody Map<String, String> assignee) {
        String newAssignee = assignee.get("assignee");
        TodoItem updatedTodo = todoService.transferTodo(id, newAssignee);
        return Result.success(updatedTodo, "转交成功");
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        Map<String, Object> statistics = todoService.getStatistics();
        return Result.success(statistics, "操作成功");
    }
}