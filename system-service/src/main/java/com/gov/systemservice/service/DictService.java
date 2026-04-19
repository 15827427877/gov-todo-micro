package com.gov.systemservice.service;

import com.gov.systemservice.pojo.Dict;

import java.util.List;

public interface DictService {
    List<Dict> getAllDicts();
    List<Dict> getDictsByType(String dictType);
    Dict getDictById(Long id);
    boolean createDict(Dict dict);
    boolean updateDict(Dict dict);
    boolean deleteDict(Long id);
}
