CREATE DATABASE IF NOT EXISTS gov_todo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gov_todo;

DROP TABLE IF EXISTS todo_item;

CREATE TABLE todo_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary key',
    title VARCHAR(255) NOT NULL COMMENT 'Todo title',
    description TEXT COMMENT 'Todo description',
    completed TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Is completed',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    INDEX idx_completed (completed),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Todo item table';

INSERT INTO todo_item (title, description, completed, create_time, update_time) VALUES
('Learn Spring Cloud', 'Study microservices architecture', 0, NOW(), NOW()),
('Implement Todo API', 'Build RESTful API for todo management', 0, NOW(), NOW()),
('Write unit tests', 'Add comprehensive unit tests', 0, NOW(), NOW());
