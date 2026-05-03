-- 通知表 - 可重复执行版本
DROP TABLE IF EXISTS notification;

CREATE TABLE notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    type VARCHAR(20) NOT NULL COMMENT '通知类型(approval/todo/system)',
    title VARCHAR(200) NOT NULL COMMENT '通知标题',
    description TEXT COMMENT '通知描述',
    `read` TINYINT DEFAULT 0 COMMENT '是否已读',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知表';

-- 创建索引
CREATE INDEX idx_notification_user_id ON notification(user_id);
CREATE INDEX idx_notification_read ON notification(`read`);
CREATE INDEX idx_notification_create_time ON notification(create_time);

-- 初始化测试数据
INSERT INTO notification (user_id, type, title, description, `read`, create_time) VALUES
(1, 'approval', '审批通过', '您提交的请假申请已通过审批', 0, '2026-05-03 14:30:00'),
(1, 'todo', '待办提醒', '您有3条待办事项需要处理', 0, '2026-05-03 10:00:00'),
(1, 'system', '系统公告', '系统将于今晚22:00进行维护升级', 1, '2026-05-02 16:00:00'),
(1, 'approval', '审批拒绝', '您提交的报销申请未通过审批，请重新提交', 0, '2026-05-03 09:15:00'),
(1, 'todo', '任务转派', '张三将"系统优化"任务转交给您', 1, '2026-05-03 08:00:00');
