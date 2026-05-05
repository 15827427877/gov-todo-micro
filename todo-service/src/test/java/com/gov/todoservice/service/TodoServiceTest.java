package com.gov.todoservice.service;

import com.gov.todoservice.mapper.TodoMapper;
import com.gov.todoservice.pojo.Activity;
import com.gov.todoservice.pojo.TodoItem;
import com.gov.todoservice.service.impl.TodoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * TodoService单元测试
 */
@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
    private TodoMapper todoMapper;

    @Mock
    private ActivityService activityService;

    private TodoItem testTodo;

    @BeforeEach
    void setUp() {
        testTodo = new TodoItem();
        testTodo.setId(1L);
        testTodo.setTitle("测试待办事项");
        testTodo.setAssignee("testuser");
        testTodo.setStatus("pending");
        testTodo.setCompleted(false);
        testTodo.setCreateTime(LocalDateTime.now());
        testTodo.setUpdateTime(LocalDateTime.now());
    }

    @Test
    void testGetAllTodos() {
        List<TodoItem> todos = Arrays.asList(testTodo);
        when(todoMapper.selectAll()).thenReturn(todos);

        List<TodoItem> result = todoService.getAllTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试待办事项", result.get(0).getTitle());
    }

    @Test
    void testGetTodoById_Found() {
        when(todoMapper.selectById(1L)).thenReturn(testTodo);

        TodoItem result = todoService.getTodoById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试待办事项", result.getTitle());
    }

    @Test
    void testGetTodoById_NotFound() {
        when(todoMapper.selectById(999L)).thenReturn(null);

        TodoItem result = todoService.getTodoById(999L);

        assertNull(result);
    }

    @Test
    void testCreateTodo() {
        TodoItem newTodo = new TodoItem();
        newTodo.setTitle("新待办事项");
        newTodo.setAssignee("testuser");

        when(todoMapper.insert(any(TodoItem.class))).thenReturn(1);

        TodoItem result = todoService.createTodo(newTodo);

        assertNotNull(result);
        assertEquals("新待办事项", result.getTitle());
        assertFalse(result.getCompleted());
    }

    @Test
    void testUpdateStatus() {
        when(todoMapper.selectById(1L)).thenReturn(testTodo);
        when(todoMapper.update(any(TodoItem.class))).thenReturn(1);

        TodoItem result = todoService.updateStatus(1L, "completed");

        assertNotNull(result);
        assertEquals("completed", result.getStatus());
    }

    @Test
    void testGetStatistics() {
        TodoItem todo1 = new TodoItem();
        todo1.setCompleted(true);
        
        TodoItem todo2 = new TodoItem();
        todo2.setCompleted(false);

        when(todoMapper.selectAll()).thenReturn(Arrays.asList(todo1, todo2));
        when(todoMapper.selectByCreateDate(anyString())).thenReturn(Arrays.asList());
        when(todoMapper.selectByCreateDateBetween(anyString(), anyString())).thenReturn(Arrays.asList());
        when(todoMapper.countByStatus(anyString())).thenReturn(0L);

        Map<String, Object> stats = todoService.getStatistics();

        assertNotNull(stats);
        assertEquals(2L, stats.get("total"));
        assertTrue(stats.containsKey("completionRate"));
        assertTrue(stats.containsKey("todayAdded"));
        assertTrue(stats.containsKey("pendingApproval"));
    }
}
