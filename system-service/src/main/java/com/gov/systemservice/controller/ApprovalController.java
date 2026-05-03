package com.gov.systemservice.controller;

import com.gov.common.Result;
import com.gov.systemservice.annotation.RequiresPermission;
import com.gov.systemservice.pojo.Approval;
import com.gov.systemservice.service.ApprovalService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    @Resource
    private ApprovalService approvalService;

    @GetMapping
    @RequiresPermission("approval:list")
    public Result<Map<String, Object>> getApprovalList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "status", defaultValue = "") String status,
            @RequestParam(value = "type", defaultValue = "") String type) {
        Map<String, Object> result = approvalService.getApprovalList(page, size, keyword, status, type);
        return Result.success(result, "操作成功");
    }

    @GetMapping("/{id}")
    @RequiresPermission("approval:read")
    public Result<Approval> getApprovalById(@PathVariable Long id) {
        Approval approval = approvalService.getApprovalById(id);
        if (approval == null) {
            return Result.error("审批记录不存在");
        }
        return Result.success(approval, "操作成功");
    }

    @PostMapping
    @RequiresPermission("approval:create")
    public Result<Approval> createApproval(@RequestBody Approval approval) {
        Approval created = approvalService.createApproval(approval);
        return Result.success(created, "创建成功");
    }

    @DeleteMapping("/{id}")
    @RequiresPermission("approval:delete")
    public Result<Boolean> deleteApproval(@PathVariable Long id) {
        boolean deleted = approvalService.deleteApproval(id);
        if (!deleted) {
            return Result.error("删除失败");
        }
        return Result.success(true, "删除成功");
    }

    @PostMapping("/{id}/approve")
    @RequiresPermission("approval:update")
    public Result<Void> approve(@PathVariable Long id, @RequestBody ApproveRequest request) {
        try {
            boolean success = approvalService.approve(id, request.getResult(), request.getComment(), 1L, "admin");
            if (!success) {
                return Result.error("审批提交失败");
            }
            return Result.success(null, "审批提交成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    public static class ApproveRequest {
        private String result;
        private String comment;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
}