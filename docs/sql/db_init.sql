-- 创建数据库
CREATE DATABASE IF NOT EXISTS gov_todo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gov_todo;

-- 用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密后）',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    department_id BIGINT COMMENT '部门ID',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(255) COMMENT '头像',
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_time DATETIME COMMENT '最后登录时间',
    login_ip VARCHAR(50) COMMENT '登录IP',
    INDEX idx_department_id (department_id),
    INDEX idx_status (status),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 部门表
DROP TABLE IF EXISTS sys_department;
CREATE TABLE sys_department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '部门ID',
    department_name VARCHAR(100) NOT NULL COMMENT '部门名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    level INT NOT NULL DEFAULT 1 COMMENT '部门级别',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序',
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 待办表
DROP TABLE IF EXISTS todo_item;
CREATE TABLE todo_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '待办ID',
    title VARCHAR(255) NOT NULL COMMENT '待办标题',
    description TEXT COMMENT '待办描述',
    user_id BIGINT COMMENT '创建用户ID',
    department_id BIGINT COMMENT '所属部门ID',
    priority TINYINT NOT NULL DEFAULT 0 COMMENT '优先级：0-低，1-中，2-高',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待办，1-进行中，2-已完成，3-已取消',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    completed_time DATETIME COMMENT '完成时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_department_id (department_id),
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_end_time (end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办表';

-- 日志表
DROP TABLE IF EXISTS sys_log;
CREATE TABLE sys_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    user_id BIGINT COMMENT '操作用户ID',
    username VARCHAR(50) COMMENT '操作用户名',
    operation VARCHAR(255) NOT NULL COMMENT '操作内容',
    method VARCHAR(255) COMMENT '操作方法',
    request_url VARCHAR(255) COMMENT '请求URL',
    request_ip VARCHAR(50) COMMENT '请求IP',
    request_param TEXT COMMENT '请求参数',
    response_time INT COMMENT '响应时间(ms)',
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-成功，0-失败',
    error_msg TEXT COMMENT '错误信息',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='日志表';

-- 字典表
DROP TABLE IF EXISTS sys_dict;
CREATE TABLE sys_dict (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '字典ID',
    dict_type VARCHAR(50) NOT NULL COMMENT '字典类型',
    dict_code VARCHAR(50) NOT NULL COMMENT '字典编码',
    dict_name VARCHAR(100) NOT NULL COMMENT '字典名称',
    dict_value VARCHAR(255) NOT NULL COMMENT '字典值',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序',
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_dict_type (dict_type),
    INDEX idx_dict_code (dict_code),
    UNIQUE KEY uk_dict_type_code (dict_type, dict_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典表';

-- 初始化数据
-- 部门数据
INSERT INTO sys_department (department_name, parent_id, level, sort, status) VALUES
('总部门', 0, 1, 1, 1),
('技术部', 1, 2, 2, 1),
('行政部', 1, 2, 3, 1),
('财务部', 1, 2, 4, 1);

-- 字典数据
INSERT INTO sys_dict (dict_type, dict_code, dict_name, dict_value, sort, status) VALUES
('todo_priority', '0', '低', '0', 1, 1),
('todo_priority', '1', '中', '1', 2, 1),
('todo_priority', '2', '高', '2', 3, 1),
('todo_status', '0', '待办', '0', 1, 1),
('todo_status', '1', '进行中', '1', 2, 1),
('todo_status', '2', '已完成', '2', 3, 1),
('todo_status', '3', '已取消', '3', 4, 1);

-- 用户数据（密码：123456，加密后）
INSERT INTO sys_user (username, password, real_name, department_id, phone, email, status) VALUES
('admin', '$2a$10$e8tF9xQJ8kQzY7g5q7Z52e7Q7e7Q7e7Q7e7Q7e7Q7e7Q7e7Q7e7', '管理员', 1, '13800138000', 'admin@example.com', 1),
('user1', '$2a$10$e8tF9xQJ8kQzY7g5q7Z52e7Q7e7Q7e7Q7e7Q7e7Q7e7Q7e7Q7e7', '用户1', 2, '13800138001', 'user1@example.com', 1),
('user2', '$2a$10$e8tF9xQJ8kQzY7g5q7Z52e7Q7e7Q7e7Q7e7Q7e7Q7e7Q7e7Q7e7', '用户2', 3, '13800138002', 'user2@example.com', 1);

-- 待办数据
INSERT INTO todo_item (title, description, user_id, department_id, priority, status, start_time, end_time) VALUES
('学习Spring Cloud', '学习微服务架构和Spring Cloud组件', 2, 2, 1, 0, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
('完成项目文档', '编写项目需求文档和技术文档', 2, 2, 2, 1, NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY)),
('参加部门会议', '参加每周部门例会', 3, 3, 0, 0, NOW(), NOW()),
('提交财务报表', '提交月度财务报表', 3, 4, 2, 0, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY));