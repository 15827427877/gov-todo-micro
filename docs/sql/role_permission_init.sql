-- 角色表
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码',
    description TEXT COMMENT '角色描述',
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 权限表
DROP TABLE IF EXISTS sys_permission;
CREATE TABLE sys_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
    permission_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    url VARCHAR(255) COMMENT '请求URL',
    method VARCHAR(10) COMMENT '请求方法',
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_permission_code (permission_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 用户角色关联表
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色权限关联表
DROP TABLE IF EXISTS sys_role_permission;
CREATE TABLE sys_role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id),
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES sys_permission(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 初始化角色数据
INSERT INTO sys_role (role_name, role_code, description, status) VALUES
('超级管理员', 'ROLE_ADMIN', '拥有系统所有权限', 1),
('普通用户', 'ROLE_USER', '拥有基础操作权限', 1),
('部门管理员', 'ROLE_DEPARTMENT_ADMIN', '拥有部门管理权限', 1);

-- 初始化权限数据
INSERT INTO sys_permission (permission_name, permission_code, url, method, status) VALUES
('用户管理', 'user:manage', '/api/users', 'GET', 1),
('角色管理', 'role:manage', '/api/roles', 'GET', 1),
('权限管理', 'permission:manage', '/api/permissions', 'GET', 1),
('部门管理', 'department:manage', '/api/departments', 'GET', 1),
('待办管理', 'todo:manage', '/api/todo', 'GET', 1),
('用户创建', 'user:create', '/api/users', 'POST', 1),
('用户编辑', 'user:update', '/api/users/*', 'PUT', 1),
('用户删除', 'user:delete', '/api/users/*', 'DELETE', 1),
('角色创建', 'role:create', '/api/roles', 'POST', 1),
('角色编辑', 'role:update', '/api/roles/*', 'PUT', 1),
('角色删除', 'role:delete', '/api/roles/*', 'DELETE', 1),
('权限创建', 'permission:create', '/api/permissions', 'POST', 1),
('权限编辑', 'permission:update', '/api/permissions/*', 'PUT', 1),
('权限删除', 'permission:delete', '/api/permissions/*', 'DELETE', 1),
('部门创建', 'department:create', '/api/departments', 'POST', 1),
('部门编辑', 'department:update', '/api/departments/*', 'PUT', 1),
('部门删除', 'department:delete', '/api/departments/*', 'DELETE', 1);

-- 初始化角色权限关联数据
-- 超级管理员拥有所有权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
(1, 6), (1, 7), (1, 8), (1, 9), (1, 10),
(1, 11), (1, 12), (1, 13), (1, 14), (1, 15),
(1, 16), (1, 17);

-- 普通用户拥有待办管理权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(2, 5);

-- 部门管理员拥有部门管理和待办管理权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
(3, 4), (3, 5), (3, 15), (3, 16), (3, 17);

-- 初始化用户角色关联数据
-- 管理员用户关联超级管理员角色
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1);

-- 普通用户关联普通用户角色
INSERT INTO sys_user_role (user_id, role_id) VALUES
(2, 2),
(3, 2);
