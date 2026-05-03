package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.annotation.RequiresPermission;
import com.gov.systemservice.pojo.Dict;
import com.gov.systemservice.service.DictService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/dicts")
public class DictsController {

    @Resource
    private DictService dictService;

    @GetMapping
    @RequiresPermission("dict:list")
    public Result<List<Dict>> getDicts() {
        List<Dict> dicts = dictService.getAllDicts();
        return Result.success(dicts);
    }

    @GetMapping("/{id}")
    @RequiresPermission("dict:read")
    public Result<Dict> getDictById(@PathVariable Long id) {
        Dict dict = dictService.getDictById(id);
        return Result.success(dict);
    }

    @GetMapping("/type/{dictType}")
    @RequiresPermission("dict:list")
    public Result<List<Dict>> getDictsByType(@PathVariable String dictType) {
        List<Dict> dicts = dictService.getDictsByType(dictType);
        return Result.success(dicts);
    }

    @PostMapping
    @RequiresPermission("dict:create")
    public Result<Dict> createDict(@RequestBody Dict dict) {
        boolean created = dictService.createDict(dict);
        if (created) {
            return Result.success(dict, "新增成功");
        }
        return Result.error("创建字典失败");
    }

    @PutMapping("/{id}")
    @RequiresPermission("dict:update")
    public Result<Dict> updateDict(@PathVariable Long id, @RequestBody Dict dict) {
        dict.setId(id);
        boolean updated = dictService.updateDict(dict);
        if (updated) {
            return Result.success(dict, "更新成功");
        }
        return Result.error("更新字典失败");
    }

    @DeleteMapping("/{id}")
    @RequiresPermission("dict:delete")
    public Result<Boolean> deleteDict(@PathVariable Long id) {
        boolean deleted = dictService.deleteDict(id);
        return Result.success(deleted, "删除成功");
    }
}