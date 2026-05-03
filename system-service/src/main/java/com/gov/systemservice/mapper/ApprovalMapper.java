package com.gov.systemservice.mapper;

import com.gov.systemservice.pojo.Approval;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApprovalMapper {

    List<Approval> selectList(@Param("keyword") String keyword,
                              @Param("status") String status,
                              @Param("type") String type);

    Approval selectById(@Param("id") Long id);

    int insert(Approval approval);

    int update(Approval approval);

    int deleteById(@Param("id") Long id);

    int count(@Param("keyword") String keyword,
              @Param("status") String status,
              @Param("type") String type);
}