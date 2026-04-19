package com.gov.systemservice.service.impl;

import com.gov.common.utils.LogUtils;
import com.gov.systemservice.mapper.DictMapper;
import com.gov.systemservice.pojo.Dict;
import com.gov.systemservice.service.DictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DictServiceImpl implements DictService {

    @Resource
    private DictMapper dictMapper;

    @Override
    public List<Dict> getAllDicts() {
        return dictMapper.selectAll();
    }

    @Override
    public List<Dict> getDictsByType(String dictType) {
        return dictMapper.selectByType(dictType);
    }

    @Override
    public Dict getDictById(Long id) {
        return dictMapper.selectById(id);
    }

    @Override
    public boolean createDict(Dict dict) {
        int result = dictMapper.insert(dict);
        if (result > 0) {
            LogUtils.info(DictServiceImpl.class, "创建字典成功: {} - {}", dict.getDictType(), dict.getDictName());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateDict(Dict dict) {
        int result = dictMapper.update(dict);
        if (result > 0) {
            LogUtils.info(DictServiceImpl.class, "更新字典成功: {} - {}", dict.getDictType(), dict.getDictName());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteDict(Long id) {
        Dict dict = dictMapper.selectById(id);
        int result = dictMapper.delete(id);
        if (result > 0) {
            LogUtils.info(DictServiceImpl.class, "删除字典成功: {} - {}", dict.getDictType(), dict.getDictName());
            return true;
        }
        return false;
    }
}
