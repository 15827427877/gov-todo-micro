package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.pojo.Dict;
import com.gov.systemservice.service.DictService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典控制器
 * 处理字典管理相关请求
 * 
 * @author chengbin
 * @since 2026-04-19
 */
@RestController
@RequestMapping("/api/dict")
public class DictController {

    @Resource
    private DictService dictService;

    /**
     * 获取字典列表
     * @return 字典列表
     */
    @GetMapping("/list")
    public Result<List<Dict>> list() {
        List<Dict> dicts = dictService.getAllDicts();
        return Result.success(dicts);
    }

    /**
     * 按类型获取字典
     * @param dictType 字典类型
     * @return 字典列表
     */
    @GetMapping("/type/{dictType}")
    public Result<List<Dict>> getByType(@PathVariable String dictType) {
        List<Dict> dicts = dictService.getDictsByType(dictType);
        return Result.success(dicts);
    }

    /**
     * 根据ID获取字典
     * @param id 字典ID
     * @return 字典信息
     */
    @GetMapping("/get/{id}")
    public Result<Dict> get(@PathVariable Long id) {
        Dict dict = dictService.getDictById(id);
        return Result.success(dict);
    }

    /**
     * 创建字典
     * @param dict 字典信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public Result<Boolean> create(@RequestBody Dict dict) {
        boolean result = dictService.createDict(dict);
        return Result.success(result);
    }

    /**
     * 更新字典
     * @param dict 字典信息
     * @return 更新结果
     */
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody Dict dict) {
        boolean result = dictService.updateDict(dict);
        return Result.success(result);
    }

    /**
     * 删除字典
     * @param id 字典ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = dictService.deleteDict(id);
        return Result.success(result);
    }
}
