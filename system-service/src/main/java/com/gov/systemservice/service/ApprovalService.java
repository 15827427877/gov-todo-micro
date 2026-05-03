package com.gov.systemservice.service;

import com.gov.systemservice.pojo.Approval;

import java.util.List;
import java.util.Map;

public interface ApprovalService {

    Map<String, Object> getApprovalList(Integer page, Integer size, String keyword, String status, String type);

    Approval getApprovalById(Long id);

    Approval createApproval(Approval approval);

    boolean deleteApproval(Long id);

    boolean approve(Long id, String result, String comment, Long approverId, String approverName);
}