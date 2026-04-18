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
}
