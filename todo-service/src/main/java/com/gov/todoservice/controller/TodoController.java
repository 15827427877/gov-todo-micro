package com.gov.todoservice.controller;

import com.gov.common.Result;
import com.gov.todoservice.pojo.TodoItem;
import com.gov.todoservice.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/list")
    public Result<List<TodoItem>> list() {
        List<TodoItem> todos = todoService.getAllTodos();
        return Result.success(todos);
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
}
