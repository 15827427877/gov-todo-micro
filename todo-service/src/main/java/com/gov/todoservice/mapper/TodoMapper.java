package com.gov.todoservice.mapper;

import com.gov.todoservice.pojo.TodoItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TodoMapper {
    List<TodoItem> selectAll();

    TodoItem selectById(@Param("id") Long id);

    int insert(TodoItem todoItem);

    int update(TodoItem todoItem);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List<Long> ids);

    List<TodoItem> selectByCreateDate(@Param("date") String date);

    List<TodoItem> selectByCreateDateBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

    Long countByStatus(@Param("status") String status);
}