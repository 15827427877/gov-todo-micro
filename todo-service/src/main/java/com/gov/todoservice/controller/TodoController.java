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
        ));
    }

    @GetMapping("/{id}")
    public Result<TodoItem> getById(@PathVariable Long id) {
        TodoItem todo = todoService.getTodoById(id);
        if (todo == null) {
            return Result.error("Todo not found");
        }
        return Result.success(todo);
    }

    @PostMapping
    public Result<TodoItem> create(@RequestBody TodoItem todoItem) {
        if (todoItem.getTitle() == null || todoItem.getTitle().trim().isEmpty()) {
            return Result.error("Title cannot be empty");
        }
        TodoItem created = todoService.createTodo(todoItem);
        return Result.success(created);
    }

    @PutMapping("/{id}")
    public Result<TodoItem> update(@PathVariable Long id, @RequestBody TodoItem todoItem) {
        try {
            TodoItem updated = todoService.updateTodo(id, todoItem);
            return Result.success(updated);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean deleted = todoService.deleteTodo(id);
        return Result.success(deleted);
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
        boolean completed = "已完成".equals(status.get("status"));
        TodoItem updatedTodo = todoService.updateStatus(id, completed);
        return Result.success(updatedTodo);
    }

    /**
     * 转交待办
     */
    @PatchMapping("/{id}/transfer")
    public Result<TodoItem> transferTodo(@PathVariable Long id, @RequestBody java.util.Map<String, String> assignee) {
        String newAssignee = assignee.get("assignee");
        TodoItem updatedTodo = todoService.transferTodo(id, newAssignee);
        return Result.success(updatedTodo);
    }

    /**
     * 待办统计
     */
    @GetMapping("/statistics")
    public Result<java.util.Map<String, Object>> statistics(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        java.util.Map<String, Object> statistics = todoService.getStatistics();
        return Result.success(statistics);
    }
}
