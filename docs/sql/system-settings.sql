-- 用户通知设置表
CREATE TABLE IF NOT EXISTS user_notification_settings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    todo_reminder TINYINT DEFAULT 1 COMMENT '待办提醒开关',
    reminder_time VARCHAR(10) DEFAULT '09:00' COMMENT '提醒时间',
    approval_notice TINYINT DEFAULT 1 COMMENT '审批通知开关',
    system_notice TINYINT DEFAULT 1 COMMENT '系统公告开关',
    notify_ways VARCHAR(100) DEFAULT '站内信' COMMENT '通知方式',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户通知设置表';

-- 登录设备表
CREATE TABLE IF NOT EXISTS user_login_device (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    device VARCHAR(100) COMMENT '设备类型',
    location VARCHAR(100) COMMENT '登录位置',
    ip VARCHAR(50) COMMENT 'IP地址',
    login_time DATETIME COMMENT '登录时间',
    is_current TINYINT DEFAULT 0 COMMENT '是否当前设备',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录设备表';

-- 登录日志表
CREATE TABLE IF NOT EXISTS login_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    device VARCHAR(100) COMMENT '设备类型',
    location VARCHAR(100) COMMENT '登录位置',
    ip VARCHAR(50) COMMENT 'IP地址',
    status VARCHAR(20) COMMENT '登录状态',
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    operation VARCHAR(100) COMMENT '操作名称',
    module VARCHAR(50) COMMENT '操作模块',
    detail TEXT COMMENT '操作详情',
    ip VARCHAR(50) COMMENT 'IP地址',
    time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';
