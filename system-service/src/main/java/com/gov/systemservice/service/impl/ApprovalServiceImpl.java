package com.gov.systemservice.service.impl;

import com.gov.systemservice.mapper.ApprovalMapper;
import com.gov.systemservice.pojo.Approval;
import com.gov.systemservice.service.ApprovalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Resource
    private ApprovalMapper approvalMapper;

    @Override
    public Map<String, Object> getApprovalList(Integer page, Integer size, String keyword, String status, String type) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 10;
        }

        List<Approval> list = approvalMapper.selectList(keyword, status, type);
        int total = list.size();

        int offset = (page - 1) * size;
        int end = Math.min(offset + size, total);
        if (offset >= total) {
            list = List.of();
        } else {
            list = list.subList(offset, end);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);

        return result;
    }

    @Override
    public Approval getApprovalById(Long id) {
        return approvalMapper.selectById(id);
    }

    @Override
    public Approval createApproval(Approval approval) {
        if (approval.getStatus() == null) {
            approval.setStatus("待审批");
        }
        approvalMapper.insert(approval);
        return approval;
    }

    @Override
    @Transactional
    public boolean deleteApproval(Long id) {
        return approvalMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public boolean approve(Long id, String result, String comment, Long approverId, String approverName) {
        Approval approval = approvalMapper.selectById(id);
        if (approval == null) {
            throw new RuntimeException("审批记录不存在");
        }

        if (!"待审批".equals(approval.getStatus())) {
            throw new RuntimeException("该审批已处理，不能重复审批");
        }

        approval.setStatus(result);
        approval.setApprover(approverName);
        approval.setApproverId(approverId);
        approval.setApproveTime(new Date());
        approval.setComment(comment);

        return approvalMapper.update(approval) > 0;
    }
}