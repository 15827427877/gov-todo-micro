-- 审批管理表
CREATE TABLE IF NOT EXISTS approval (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '审批ID',
    title VARCHAR(255) NOT NULL COMMENT '审批标题',
    type VARCHAR(50) NOT NULL COMMENT '审批类型',
    applicant VARCHAR(100) NOT NULL COMMENT '申请人',
    applicant_id BIGINT COMMENT '申请人ID',
    department VARCHAR(100) COMMENT '所属部门',
    content TEXT COMMENT '申请内容',
    status VARCHAR(20) DEFAULT '待审批' COMMENT '审批状态',
    approver VARCHAR(100) COMMENT '审批人',
    approver_id BIGINT COMMENT '审批人ID',
    approve_time DATETIME COMMENT '审批时间',
    comment TEXT COMMENT '审批说明',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_applicant (applicant_id),
    INDEX idx_status (status),
    INDEX idx_type (type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审批管理表';

-- 初始化审批类型字典数据
INSERT INTO sys_dict (dict_type, dict_code, dict_name, dict_value, sort, status) VALUES
('approval_type', 'leave', '请假申请', '请假申请', 1, 1),
('approval_type', 'reimburse', '报销申请', '报销申请', 2, 1),
('approval_type', 'overtime', '加班申请', '加班申请', 3, 1),
('approval_type', 'travel', '出差申请', '出差申请', 4, 1),
('approval_type', 'purchase', '采购申请', '采购申请', 5, 1),
('approval_type', 'other', '其他申请', '其他申请', 6, 1);

-- 初始化审批状态字典数据
INSERT INTO sys_dict (dict_type, dict_code, dict_name, dict_value, sort, status) VALUES
('approval_status', 'pending', '待审批', '待审批', 1, 1),
('approval_status', 'approved', '已通过', '已通过', 2, 1),
('approval_status', 'rejected', '已拒绝', '已拒绝', 3, 1);

-- 初始化示例审批数据
INSERT INTO approval (title, type, applicant, applicant_id, department, content, status) VALUES
('张三的请假申请', '请假申请', '张三', 2, '技术部', '请假原因：家中有事\n请假时间：2026-05-05 至 2026-05-06', '待审批'),
('李四的报销申请', '报销申请', '李四', 3, '行政部', '报销金额：1500元\n报销事由：办公用品采购', '待审批'),
('王五的加班申请', '加班申请', '王五', 4, '财务部', '加班时间：2026-05-04 18:00-22:00\n加班原因：月度报表整理', '已通过');
