package com.gov.systemservice.mapper;

import com.gov.systemservice.pojo.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictMapper {
    List<Dict> selectAll();
    List<Dict> selectByType(@Param("dictType") String dictType);
    Dict selectById(@Param("id") Long id);
    int insert(Dict dict);
    int update(Dict dict);
    int delete(@Param("id") Long id);
}
