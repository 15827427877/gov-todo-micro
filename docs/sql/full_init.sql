-- ============================================
-- 完整的数据库初始化脚本
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS gov_todo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gov_todo;

-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 1. 删除已存在的表（按外键依赖顺序）
-- ============================================
DROP TABLE IF EXISTS sys_activity_log;
DROP TABLE IF EXISTS sys_log;
DROP TABLE IF EXISTS todo_item;
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS sys_role_permission;
DROP TABLE IF EXISTS sys_permission;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS sys_department;
DROP TABLE IF EXISTS sys_dict;

-- ============================================
-- 2. 创建表结构
-- ============================================

-- 用户表
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
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
CREATE TABLE sys_department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '部门ID',
    name VARCHAR(100) NOT NULL COMMENT '部门名称',
    leader VARCHAR(50) COMMENT '负责人',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    level INT NOT NULL DEFAULT 1 COMMENT '部门级别',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序',
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 角色表
CREATE TABLE sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    description VARCHAR(255) COMMENT '角色描述',
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_role_code (role_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 权限表（树形结构）
CREATE TABLE sys_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父权限ID',
    permission_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(100) NOT NULL COMMENT '权限编码',
    url VARCHAR(255) COMMENT '权限URL',
    method VARCHAR(20) COMMENT '请求方法',
    description VARCHAR(255) COMMENT '权限描述',
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_permission_code (permission_code),
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 角色权限关联表
CREATE TABLE sys_role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 用户角色关联表
CREATE TABLE sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 待办事项表
CREATE TABLE todo_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '待办ID',
    title VARCHAR(255) NOT NULL COMMENT '待办标题',
    assignee VARCHAR(50) COMMENT '负责人',
    status VARCHAR(20) NOT NULL DEFAULT '待处理' COMMENT '状态：待处理、处理中、已完成',
    deadline VARCHAR(50) COMMENT '截止日期',
    description TEXT COMMENT '待办描述',
    completed BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否完成',
    user_id BIGINT COMMENT '创建用户ID',
    department_id BIGINT COMMENT '所属部门ID',
    priority TINYINT NOT NULL DEFAULT 0 COMMENT '优先级：0-低，1-中，2-高',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    completed_time DATETIME COMMENT '完成时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_department_id (department_id),
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_end_time (end_time),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项表';

-- 活动日志表
CREATE TABLE sys_activity_log (
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

-- 操作日志表
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 字典表
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

-- ============================================
-- 3. 初始化数据
-- ============================================

-- 部门数据
INSERT INTO sys_department (name, leader, parent_id, level, sort, status) VALUES
('总部门', '张三', 0, 1, 1, 1),
('技术部', '李四', 1, 2, 2, 1),
('行政部', '王五', 1, 2, 3, 1),
('财务部', '赵六', 1, 2, 4, 1);

-- 字典数据
INSERT INTO sys_dict (dict_type, dict_code, dict_name, dict_value, sort, status) VALUES
('todo_priority', '0', '低', '0', 1, 1),
('todo_priority', '1', '中', '1', 2, 1),
('todo_priority', '2', '高', '2', 3, 1),
('todo_status', '待处理', '待处理', '待处理', 1, 1),
('todo_status', '处理中', '处理中', '处理中', 2, 1),
('todo_status', '已完成', '已完成', '已完成', 3, 1);

-- 角色数据
INSERT INTO sys_role (role_name, role_code, description, status) VALUES
('超级管理员', 'ADMIN', '系统超级管理员，拥有所有权限', 1),
('普通用户', 'USER', '普通用户，拥有基本操作权限', 1);

-- 权限数据（树形结构）
-- 一级权限：系统管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(0, '系统管理', 'system:manage', NULL, NULL, '系统管理模块', 1);

-- 二级权限：认证管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(1, '认证管理', 'auth:manage', NULL, NULL, '认证管理模块', 1);

-- 三级权限：认证操作
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(2, '用户登录', 'auth:login', '/api/login', 'POST', '用户登录', 1),
(2, '用户注册', 'auth:register', '/api/system/user/register', 'POST', '用户注册', 1),
(2, '获取用户信息', 'auth:info', '/api/system/user/info', 'GET', '获取用户信息', 1);

-- 二级权限：待办管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(1, '待办管理', 'todo:manage', NULL, NULL, '待办管理模块', 1);

-- 三级权限：待办操作
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(5, '待办列表', 'todo:list', '/api/todo/list', 'GET', '获取待办列表', 1),
(5, '待办详情', 'todo:read', '/api/todo/{id}', 'GET', '获取待办详情', 1),
(5, '新增待办', 'todo:create', '/api/todo', 'POST', '新增待办', 1),
(5, '更新待办', 'todo:update', '/api/todo/{id}', 'PUT', '更新待办', 1),
(5, '删除待办', 'todo:delete', '/api/todo/{id}', 'DELETE', '删除待办', 1),
(5, '更改状态', 'todo:status', '/api/todo/{id}/status', 'PATCH', '更改待办状态', 1),
(5, '转交待办', 'todo:transfer', '/api/todo/{id}/transfer', 'PATCH', '转交待办', 1),
(5, '待办统计', 'todo:statistics', '/api/todo/statistics', 'GET', '待办统计', 1);

-- 二级权限：用户管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(1, '用户管理', 'user:manage', NULL, NULL, '用户管理模块', 1);

-- 三级权限：用户操作
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(13, '用户列表', 'user:list', '/api/users', 'GET', '获取用户列表', 1),
(13, '用户详情', 'user:read', '/api/users/{id}', 'GET', '获取用户详情', 1),
(13, '用户创建', 'user:create', '/api/users', 'POST', '创建用户', 1),
(13, '用户更新', 'user:update', '/api/users/{id}', 'PUT', '更新用户', 1),
(13, '用户删除', 'user:delete', '/api/users/{id}', 'DELETE', '删除用户', 1),
(13, '用户角色', 'user:roles', '/api/users/{id}/roles', 'GET', '获取用户角色', 1),
(13, '分配用户角色', 'user:assign', '/api/users/{id}/roles', 'POST', '分配用户角色', 1);

-- 二级权限：角色管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(1, '角色管理', 'role:manage', NULL, NULL, '角色管理模块', 1);

-- 三级权限：角色操作
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(21, '角色列表', 'role:list', '/api/roles', 'GET', '获取角色列表', 1),
(21, '角色详情', 'role:read', '/api/roles/{id}', 'GET', '获取角色详情', 1),
(21, '角色创建', 'role:create', '/api/roles', 'POST', '创建角色', 1),
(21, '角色更新', 'role:update', '/api/roles/{id}', 'PUT', '更新角色', 1),
(21, '角色删除', 'role:delete', '/api/roles/{id}', 'DELETE', '删除角色', 1),
(21, '角色权限列表', 'role:permissions', '/api/roles/{id}/permissions', 'GET', '获取角色权限列表', 1),
(21, '分配角色权限', 'role:assign', '/api/roles/{id}/permissions', 'POST', '分配角色权限', 1);

-- 二级权限：部门管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(1, '部门管理', 'department:manage', NULL, NULL, '部门管理模块', 1);

-- 三级权限：部门操作
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(29, '部门列表', 'department:list', '/api/departments', 'GET', '获取部门列表', 1),
(29, '部门详情', 'department:read', '/api/departments/{id}', 'GET', '获取部门详情', 1),
(29, '部门创建', 'department:create', '/api/departments', 'POST', '创建部门', 1),
(29, '部门更新', 'department:update', '/api/departments/{id}', 'PUT', '更新部门', 1),
(29, '部门删除', 'department:delete', '/api/departments/{id}', 'DELETE', '删除部门', 1);

-- 二级权限：权限管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(1, '权限管理', 'permission:manage', NULL, NULL, '权限管理模块', 1);

-- 三级权限：权限操作
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(35, '权限列表', 'permission:list', '/api/permissions', 'GET', '获取权限列表', 1),
(35, '权限树形', 'permission:tree', '/api/permissions/tree', 'GET', '获取权限树形结构', 1),
(35, '权限详情', 'permission:read', '/api/permissions/{id}', 'GET', '获取权限详情', 1),
(35, '权限创建', 'permission:create', '/api/permissions', 'POST', '创建权限', 1),
(35, '权限更新', 'permission:update', '/api/permissions/{id}', 'PUT', '更新权限', 1),
(35, '权限删除', 'permission:delete', '/api/permissions/{id}', 'DELETE', '删除权限', 1);

-- 二级权限：模块管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(1, '模块管理', 'module:manage', NULL, NULL, '模块管理模块', 1);

-- 三级权限：模块操作
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(41, '模块列表', 'module:list', '/api/modules', 'GET', '获取模块列表', 1);

-- 二级权限：活动管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(1, '活动管理', 'activity:manage', NULL, NULL, '活动管理模块', 1);

-- 三级权限：活动操作
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(44, '活动列表', 'activity:list', '/api/activities/recent', 'GET', '获取最近活动', 1),
(44, '记录活动', 'activity:record', '/api/activities/record', 'POST', '记录活动', 1);

-- 二级权限：字典管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(1, '字典管理', 'dict:manage', NULL, NULL, '字典管理模块', 1);

-- 三级权限：字典操作
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(47, '字典列表', 'dict:list', '/api/dict/list', 'GET', '获取字典列表', 1),
(47, '按类型获取字典', 'dict:type', '/api/dict/type/{dictType}', 'GET', '按类型获取字典', 1);

-- 用户数据（密码：123456，BCrypt加密后）
INSERT INTO sys_user (username, password, real_name, department_id, phone, email, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6E9l.', '管理员', 1, '13800138000', 'admin@example.com', 1),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6E9l.', '用户1', 2, '13800138001', 'user1@example.com', 1),
('user2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6E9l.', '用户2', 3, '13800138002', 'user2@example.com', 1);

-- 为超级管理员角色分配所有权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission;

-- 为普通用户角色分配基本权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, id FROM sys_permission WHERE permission_code IN (
    'auth:login',
    'auth:register',
    'auth:info',
    'todo:list',
    'todo:read',
    'todo:create',
    'todo:update',
    'todo:status',
    'todo:transfer',
    'todo:statistics',
    'user:list',
    'user:read',
    'department:list',
    'department:read',
    'activity:list',
    'dict:list',
    'dict:type'
);

-- 为admin用户分配超级管理员角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 为user1和user2分配普通用户角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (2, 2), (3, 2);

-- 待办数据
INSERT INTO todo_item (title, assignee, status, deadline, description, completed, user_id, department_id, priority, start_time, end_time) VALUES
('学习Spring Cloud', 'user1', '待处理', DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 7 DAY), '%Y-%m-%d'), '学习微服务架构和Spring Cloud组件', FALSE, 2, 2, 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
('完成项目文档', 'user1', '处理中', DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 3 DAY), '%Y-%m-%d'), '编写项目需求文档和技术文档', FALSE, 2, 2, 2, NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY)),
('参加部门会议', 'user2', '待处理', DATE_FORMAT(NOW(), '%Y-%m-%d'), '参加每周部门例会', FALSE, 3, 3, 0, NOW(), NOW()),
('提交财务报表', 'user2', '待处理', DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), '提交月度财务报表', FALSE, 3, 4, 2, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY));

-- 活动日志示例数据
INSERT INTO sys_activity_log (user_id, user_name, title, content, action_type, status, status_type, icon, related_id, related_type) VALUES
(1, 'admin', '创建了待办事项', '创建了待办事项：学习Spring Cloud', 'CREATE', '成功', 'success', 'el-icon-plus', 1, 'TODO'),
(1, 'admin', '更新了待办状态', '将待办事项"完成项目文档"状态更新为"处理中"', 'STATUS_CHANGE', '处理中', 'primary', 'el-icon-edit', 2, 'TODO'),
(2, 'user1', '创建了待办事项', '创建了待办事项：参加部门会议', 'CREATE', '成功', 'success', 'el-icon-plus', 3, 'TODO');

-- 启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 初始化完成
-- ============================================
