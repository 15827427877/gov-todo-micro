
# 开发规范指南

为保证代码质量、可维护性、安全性与可扩展性，请在开发过程中严格遵循以下规范。

## 一、项目基本信息

### 1.1 环境配置
- **操作系统**：Windows 11
- **工作区路径**：`D:\IDEA-WorkSpace\gov-todo-micro`
- **JDK 版本**：JDK 21.0.10
- **构建工具**：Maven 3.x
- **代码作者**：MECHREVO

### 1.2 技术栈要求
- **主框架**：Spring Boot 2.7.15 (基于 Spring Cloud 2021.0.5)
- **语言版本**：Java 21
- **核心依赖**：
  - `spring-boot-starter-web` (或 WebFlux for Gateway)
  - `spring-boot-starter-data-jpa` (未配置，实际使用 MyBatis)
  - `mybatis-spring-boot-starter` (2.3.2)
  - `spring-cloud-starter-alibaba-nacos-discovery`
  - `spring-cloud-starter-alibaba-sentinel`
  - `spring-cloud-starter-openfeign`
  - `lombok` (1.18.30)
  - `spring-boot-starter-data-redis`

## 二、目录结构规范

项目采用 **Maven 多模块聚合** 结构，各服务模块应严格遵循以下标准目录布局：

```
gov-todo-micro/
├── common/                      # 公共模块 (依赖: Security, JWT, Redis, Web)
│   └── src/main/java/com/gov/common/
│       ├── config/
│       └── utils/
├── gateway-server/              # 网关服务 (Reactive: Netty)
│   └── src/main/java/com/gov/gatewayserver/
│       ├── config/
│       └── filter/
├── system-service/              # 系统服务 (Servlet: Tomcat)
│   └── src/main/java/com/gov/systemservice/
│       ├── annotation/          # 自定义注解
│       ├── aspect/              # AOP切面
│       ├── config/
│       ├── controller/
│       ├── dto/                 # 数据传输对象
│       ├── mapper/              # MyBatis Mapper接口
│       ├── pojo/                # 数据库实体
│       ├── service/
│       │   └── impl/            # Service实现类
│       └── utils/
│       └── resources/
│           └── mapper/          # MyBatis XML映射文件
├── todo-service/                # 待办服务 (Servlet: Tomcat)
│   └── src/main/java/com/gov/todoservice/
│       ├── config/
│       ├── controller/
│       ├── exception/           # 全局异常处理
│       ├── mapper/              # MyBatis Mapper接口
│       ├── pojo/                # 数据库实体
│       ├── service/
│       │   └── impl/            # Service实现类
│       └── resources/
│           └── mapper/          # MyBatis XML映射文件
└── docs/                        # 项目文档
```

## 三、分层架构规范

| 层级        | 职责说明                         | 开发约束与注意事项                                               |
|-------------|----------------------------------|----------------------------------------------------------------|
| **Controller** | 处理 HTTP 请求与响应，定义 API 接口 | 不得直接访问数据库，必须通过 Service 层调用                  |
| **Service**    | 实现业务逻辑、事务管理与数据校验   | 必须通过 Mapper 层访问数据库；返回 DTO 而非 Entity（除非必要） |
| **Mapper**     | 数据库访问与持久化操作             | 继承 `org.apache.ibatis.annotations.Mapper` 或 `BaseMapper`   |
| **Entity**     | 映射数据库表结构                   | 不得直接返回给前端（需转换为 DTO）；包名建议为 `pojo`         |

### 接口与实现分离
- 所有业务逻辑通过接口定义（如 `UserService`），具体实现放在 `impl` 子包中。
- MyBatis Mapper 接口通常直接位于对应包下，无需额外 `impl` 子包。

## 四、依赖与依赖管理

### 4.1 通用依赖规则
- **MyBatis 配置**：启用驼峰命名自动转换 (`map-underscore-to-camel-case: true`)。
- **日志规范**：
  - 使用 `@Slf4j` 注解代替 `System.out.println`。
  - Mapper 层日志级别设为 `debug` (`com.gov.*.mapper: debug`)。
  - Gateway 依赖 `logstash-logback-encoder` (7.2) 进行 JSON 格式化日志输出。
- **Redis 连接池**：使用 Lettuce (Spring Boot 默认配置)。

### 4.2 模块依赖说明
- **Common 模块**：
  - 提供 Spring Security、JWT、Redis、Web 基础能力。
  - **Gateway 模块**在使用 Common 时，必须手动排除 `spring-boot-starter-web` 和 `spring-boot-starter-tomcat`，因为 Gateway 使用 `spring-boot-starter-webflux` (Netty)。
- **Service 模块**：
  - 均依赖 `common` 模块。
  - 均引入 `mybatis-spring-boot-starter` (2.3.2)。
  - 均引入 `spring-cloud-starter-alibaba-nacos-discovery` 和 `sentinel`。

## 五、安全与性能规范

### 5.1 输入校验
- 使用 JSR-303 校验注解（如 `@NotBlank`, `@Size` 等）。
- 注意：Spring Boot 2.7.x 仍使用 `javax.validation.constraints.*`。

### 5.2 事务管理
- `@Transactional` 注解仅用于 **Service 层**方法。
- 避免在循环中频繁提交事务，影响性能。

### 5.3 网关路由与白名单
- 网关路径重写规则：
  - `/api/login` -> `http://localhost:8091/api/system/user/login`
  - `/api/register` -> `http://localhost:8091/api/system/user/register`
- **白名单配置** (`auth.whitelist`)：包含登录/注册接口及密码重置接口。

## 六、代码风格规范

### 6.1 命名规范
| 类型       | 命名方式             | 示例                  |
|------------|----------------------|-----------------------|
| 类名       | UpperCamelCase       | `UserServiceImpl`     |
| 方法/变量  | lowerCamelCase       | `saveUser()`          |
| 常量       | UPPER_SNAKE_CASE     | `MAX_LOGIN_ATTEMPTS`  |
| Mapper     | 以 Mapper 结尾       | `UserMapper`          |

### 6.2 注释规范
- 所有类、方法、字段需添加 **Javadoc** 注释。
- 注释语言：中文。

### 6.3 类型命名规范（阿里巴巴风格）
| 后缀 | 用途说明                     | 示例         |
|------|------------------------------|--------------|
| DTO  | 数据传输对象                 | `UserDTO`    |
| DO/Pojo | 数据库实体对象               | `UserPojo`   |
| BO   | 业务逻辑封装对象             | `UserBO`     |
| VO   | 视图展示对象                 | `UserVO`     |
| Query| 查询参数封装对象             | `UserQuery`  |

### 6.4 实体类简化工具
- 使用 Lombok 注解替代手动编写 getter/setter/构造方法：
  - `@Data`
  - `@NoArgsConstructor`
  - `@AllArgsConstructor`

## 七、扩展性与日志规范

### 7.1 接口优先原则
- 所有业务逻辑通过接口定义（如 `UserService`），具体实现放在 `impl` 包中。

### 7.2 日志记录
- **Service/Controller 层**：使用 `@Slf4j` 记录关键业务流程。
- **Gateway 层**：配置 `logstash-logback-encoder` 适配 ELK 或类似日志收集系统。

## 八、编码原则总结
| 原则       | 说明                                       |
|------------|--------------------------------------------|
| **SOLID**  | 高内聚、低耦合，增强可维护性与可扩展性     |
| **DRY**    | 避免重复代码，提高复用性                   |
| **KISS**   | 保持代码简洁易懂                           |
| **YAGNI**  | 不实现当前不需要的功能                     |
| **OWASP**  | 防范常见安全漏洞，如 SQL 注入、XSS 等      |
