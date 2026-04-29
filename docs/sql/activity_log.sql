-- 活动日志表
CREATE TABLE IF NOT EXISTS sys_activity_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '活动ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    user_name VARCHAR(100) COMMENT '用户名',
    title VARCHAR(500) NOT NULL COMMENT '活动标题',
    content TEXT COMMENT '活动内容',
    action_type VARCHAR(50) COMMENT '动作类型：CREATE/UPDATE/DELETE/STATUS_CHANGE/TRANSFER',
    status VARCHAR(50) COMMENT '关联状态',
    status_type VARCHAR(50) COMMENT '状态类型：success/primary/info/warning/danger',
    icon VARCHAR(100) COMMENT '图标',
    related_id BIGINT COMMENT '关联ID（如待办ID）',
    related_type VARCHAR(50) COMMENT '关联类型（如TODO）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time),
    INDEX idx_action_type (action_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动日志表';