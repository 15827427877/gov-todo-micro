package com.gov.todoservice.service.impl;

import com.gov.todoservice.mapper.TodoMapper;
import com.gov.todoservice.pojo.TodoItem;
import com.gov.todoservice.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoMapper todoMapper;

    @Override
    public List<TodoItem> getAllTodos() {
        return todoMapper.selectAll();
    }

    @Override
    public TodoItem getTodoById(Long id) {
        return todoMapper.selectById(id);
    }

    @Override
    @Transactional
    public TodoItem createTodo(TodoItem todoItem) {
        todoItem.setCreateTime(LocalDateTime.now());
        todoItem.setUpdateTime(LocalDateTime.now());
        todoItem.setCompleted(false);
        todoMapper.insert(todoItem);
        return todoItem;
    }

    @Override
    @Transactional
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
        return existingTodo;
    }

    @Override
    @Transactional
    public boolean deleteTodo(Long id) {
        return todoMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public boolean deleteTodos(List<Long> ids) {
        return todoMapper.deleteByIds(ids) > 0;
    }

    @Override
    @Transactional
    public TodoItem updateStatus(Long id, String status) {
        TodoItem existingTodo = todoMapper.selectById(id);
        if (existingTodo == null) {
            throw new RuntimeException("Todo not found with id: " + id);
        }
        existingTodo.setStatus(status);
        // 根据 status 字段更新 completed 字段
        existingTodo.setCompleted("已完成".equals(status));
        existingTodo.setUpdateTime(java.time.LocalDateTime.now());
        todoMapper.update(existingTodo);
        return existingTodo;
    }

    @Override
    @Transactional
    public TodoItem transferTodo(Long id, String assignee) {
        TodoItem existingTodo = todoMapper.selectById(id);
        if (existingTodo == null) {
            throw new RuntimeException("Todo not found with id: " + id);
        }
        existingTodo.setAssignee(assignee);
        existingTodo.setUpdateTime(java.time.LocalDateTime.now());
        todoMapper.update(existingTodo);
        return existingTodo;
    }

    @Override
    public java.util.Map<String, Object> getStatistics() {
        List<TodoItem> allTodos = todoMapper.selectAll();
        long total = allTodos.size();
        long completed = allTodos.stream().filter(todo -> todo.getCompleted() != null && todo.getCompleted()).count();
        long pending = total - completed;
        
        java.util.Map<String, Object> statistics = new java.util.HashMap<>();
        statistics.put("total", total);
        statistics.put("completed", completed);
        statistics.put("pending", pending);
        statistics.put("completedRate", total > 0 ? (double) completed / total : 0.0);
        
        return statistics;
    }
}
