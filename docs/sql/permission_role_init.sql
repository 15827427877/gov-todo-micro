-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 用户角色关联表（先删除，有外键引用）
DROP TABLE IF EXISTS sys_user_role;

-- 角色权限关联表（先删除，有外键引用）
DROP TABLE IF EXISTS sys_role_permission;

-- 角色表
DROP TABLE IF EXISTS sys_role;
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

-- 权限表
DROP TABLE IF EXISTS sys_permission;
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

-- 初始化角色数据
INSERT INTO sys_role (role_name, role_code, description, status) VALUES
('超级管理员', 'ADMIN', '系统超级管理员，拥有所有权限', 1),
('普通用户', 'USER', '普通用户，拥有基本操作权限', 1);

-- 初始化权限数据（树形结构）
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
(13, '用户删除', 'user:delete', '/api/users/{id}', 'DELETE', '删除用户', 1);

-- 二级权限：角色管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(1, '角色管理', 'role:manage', NULL, NULL, '角色管理模块', 1);

-- 三级权限：角色操作
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(19, '角色列表', 'role:list', '/api/roles', 'GET', '获取角色列表', 1),
(19, '角色详情', 'role:read', '/api/roles/{id}', 'GET', '获取角色详情', 1),
(19, '角色创建', 'role:create', '/api/roles', 'POST', '创建角色', 1),
(19, '角色更新', 'role:update', '/api/roles/{id}', 'PUT', '更新角色', 1),
(19, '角色删除', 'role:delete', '/api/roles/{id}', 'DELETE', '删除角色', 1),
(19, '角色权限列表', 'role:permissions', '/api/roles/{id}/permissions', 'GET', '获取角色权限列表', 1),
(19, '分配角色权限', 'role:assign', '/api/roles/{id}/permissions', 'POST', '分配角色权限', 1);

-- 二级权限：部门管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(1, '部门管理', 'department:manage', NULL, NULL, '部门管理模块', 1);

-- 三级权限：部门操作
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(27, '部门列表', 'department:list', '/api/departments', 'GET', '获取部门列表', 1),
(27, '部门详情', 'department:read', '/api/departments/{id}', 'GET', '获取部门详情', 1),
(27, '部门创建', 'department:create', '/api/departments', 'POST', '创建部门', 1),
(27, '部门更新', 'department:update', '/api/departments/{id}', 'PUT', '更新部门', 1),
(27, '部门删除', 'department:delete', '/api/departments/{id}', 'DELETE', '删除部门', 1);

-- 二级权限：权限管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(1, '权限管理', 'permission:manage', NULL, NULL, '权限管理模块', 1);

-- 三级权限：权限操作
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(33, '权限列表', 'permission:list', '/api/permissions', 'GET', '获取权限列表', 1),
(33, '权限详情', 'permission:read', '/api/permissions/{id}', 'GET', '获取权限详情', 1),
(33, '权限创建', 'permission:create', '/api/permissions', 'POST', '创建权限', 1),
(33, '权限更新', 'permission:update', '/api/permissions/{id}', 'PUT', '更新权限', 1),
(33, '权限删除', 'permission:delete', '/api/permissions/{id}', 'DELETE', '删除权限', 1);

-- 二级权限：模块管理
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(1, '模块管理', 'module:manage', NULL, NULL, '模块管理模块', 1);

-- 三级权限：模块操作
INSERT INTO sys_permission (parent_id, permission_name, permission_code, url, method, description, status) VALUES
(39, '模块列表', 'module:list', '/api/modules', 'GET', '获取模块列表', 1);

-- 为超级管理员角色分配所有权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission;

-- 为普通用户角色分配基本权限（只分配查询类权限和登录权限）
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
    'user:list',
    'user:read',
    'department:list',
    'department:read'
);

-- 为admin用户分配超级管理员角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 启用外键检查
SET FOREIGN_KEY_CHECKS = 1;