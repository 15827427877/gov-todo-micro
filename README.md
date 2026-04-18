# Gov Todo Micro - 微服务政务待办事项管理系统

## 项目概述

Gov Todo Micro 是一个基于 Spring Cloud 的微服务架构待办事项管理系统，采用分布式设计理念，为政府办公提供高效、可扩展的待办管理解决方案。

## 技术栈

- **Java**: 项目主要开发语言
- **Spring Boot**: 应用框架
- **Spring Cloud Gateway**: API 网关
- **MyBatis**: 持久层框架
- **Maven**: 项目构建工具
- **MySQL**: 关系型数据库

## 项目架构

```
gov-todo-micro/
├── gateway-server/          # API 网关服务
├── system-service/          # 系统管理服务
├── todo-service/            # 待办事项服务
├── common/                  # 公共模块
├── scripts/                 # 数据库脚本
└── pom.xml                 # 父 POM 文件
```

## 模块说明

### 1. gateway-server (网关服务)
- API 网关入口
- 路由转发
- 负载均衡
- 统一鉴权（可扩展）

### 2. system-service (系统管理服务)
- 用户管理
- 角色权限管理
- 系统配置管理

### 3. todo-service (待办事项服务)
- 待办事项增删改查
- 待办状态管理
- 待办分类管理
- 包含完整的 MVC 架构：
  - **Controller**: `TodoController` - RESTful API 接口
  - **Service**: `TodoService` / `TodoServiceImpl` - 业务逻辑
  - **Mapper**: `TodoMapper` - 数据访问层
  - **POJO**: `TodoItem` - 数据实体
  - **Exception**: `GlobalExceptionHandler` - 全局异常处理

### 4. common (公共模块)
- 通用工具类
- 统一返回结果封装 (`Result.java`)
- 公共常量定义

## 数据库设计

数据库初始化脚本位于 `scripts/init.sql`

## 快速开始

### 环境要求

- JDK 8+
- Maven 3.6+
- MySQL 5.7+

### 安装步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd gov-todo-micro
```

2. **初始化数据库**
```bash
mysql -u root -p < scripts/init.todo_item.sql
```

3. **修改配置**

根据需要修改各服务的配置文件：
- `gateway-server/src/main/resources/application.yml`
- `system-service/src/main/resources/application.yml`
- `todo-service/src/main/resources/application.yml`

4. **编译项目**
```bash
mvn clean install
```

5. **启动服务**

按以下顺序启动服务：

```bash
# 启动网关服务
cd gateway-server
mvn spring-boot:run

# 启动系统服务（新终端）
cd system-service
mvn spring-boot:run

# 启动待办服务（新终端）
cd todo-service
mvn spring-boot:run
```

### 服务端口

各服务端口在 `application.yml` 中配置，典型配置如下：
- Gateway Server: 8080
- System Service: 8081
- Todo Service: 8082

## API 接口

### 待办事项接口 (通过网关访问)

```
GET    /api/todos          # 获取所有待办
POST   /api/todos          # 创建待办
PUT    /api/todos/{id}     # 更新待办
DELETE /api/todos/{id}     # 删除待办
GET    /api/todos/{id}     # 获取单个待办
```

## 开发指南

### 代码结构规范

每个业务服务遵循标准的分层架构：
```
service/
├── controller/      # 控制层
├── service/         # 业务层
│   └── impl/       # 业务实现
├── mapper/         # 数据访问层
├── pojo/           # 实体类
└── exception/      # 异常处理
```

### 统一返回格式

所有接口返回统一使用 `Result` 对象封装：
```java
{
    "code": 200,
    "message": "success",
    "data": {}
}
```

## 项目配置

- **父 POM**: 统一管理依赖版本
- **Settings**: `settings.xml` - Maven 配置
- **依赖管理**: 集中在父 POM 中管理

## 扩展建议

- [ ] 集成 Nacos/Eureka 服务注册与发现
- [ ] 集成 Sentinel 实现服务限流降级
- [ ] 添加 Spring Security 统一认证
- [ ] 集成 Redis 缓存
- [ ] 添加日志收集系统 (ELK)
- [ ] 完善单元测试覆盖率
- [ ] 添加 Docker 支持
- [ ] 集成配置中心

## 许可证

本项目仅供学习和参考使用。

## 联系方式

如有问题，请提交 Issue 或联系项目维护者。
