package com.gov.todoservice.service;

import com.gov.todoservice.pojo.TodoItem;
import java.util.List;

import java.util.Map;

public interface TodoService {

    List<TodoItem> getAllTodos();

    TodoItem getTodoById(Long id);

    TodoItem createTodo(TodoItem todoItem);

    TodoItem updateTodo(Long id, TodoItem todoItem);

    boolean deleteTodo(Long id);

    boolean deleteTodos(List<Long> ids);

    TodoItem updateStatus(Long id, boolean completed);

    TodoItem transferTodo(Long id, String assignee);

    Map<String, Object> getStatistics();
}
