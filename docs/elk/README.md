# ELK日志收集系统部署指南

## 概述

本文档介绍如何使用Docker Compose部署ELK(Elasticsearch + Logstash + Kibana)日志收集系统,用于gov-todo-micro微服务项目的日志集中管理和分析。

## 系统要求

- Docker 20.10+
- Docker Compose 2.0+
- 至少4GB可用内存
- 至少10GB可用磁盘空间

## 快速开始

### 1. 启动ELK服务

```bash
cd docs/elk
docker-compose up -d
```

### 2. 验证服务状态

```bash
docker-compose ps
```

预期输出:
```
NAME                STATUS         PORTS
elk-elasticsearch   Up             0.0.0.0:9200->9200/tcp, 0.0.0.0:9300->9300/tcp
elk-logstash        Up             0.0.0.0:5044->5044/tcp, 0.0.0.0:9600->9600/tcp
elk-kibana          Up             0.0.0.0:5601->5601/tcp
```

### 3. 验证Elasticsearch

```bash
curl http://localhost:9200
```

### 4. 访问Kibana

浏览器访问: http://localhost:5601

## 配置Kibana

### 1. 创建索引模式

1. 访问Kibana: http://localhost:5601
2. 进入 **Stack Management** > **Index Patterns**
3. 点击 **Create index pattern**
4. 输入索引模式: `gov-todo-*`
5. 选择时间字段: `@timestamp`
6. 点击 **Create index pattern**

### 2. 查看日志

1. 进入 **Discover** 页面
2. 选择索引模式 `gov-todo-*`
3. 可以使用以下过滤条件:
   - `service_name: "gateway-server"` - 查看网关日志
   - `service_name: "system-service"` - 查看系统服务日志
   - `service_name: "todo-service"` - 查看待办服务日志
   - `level: "ERROR"` - 查看错误日志

## 日志查询示例

### Kibana查询语法

```
# 查询特定服务的错误日志
service_name: "system-service" AND level: "ERROR"

# 查询特定时间范围的日志
@timestamp: [now-1h TO now]

# 查询包含特定关键字的日志
message: "用户登录"

# 组合查询
service_name: "gateway-server" AND message: "认证失败" AND level: "WARN"
```

### 常用查询场景

1. **查看最近1小时的错误日志**
   ```
   level: "ERROR" AND @timestamp: [now-1h TO now]
   ```

2. **查看特定用户的所有操作**
   ```
   message: "username"
   ```

3. **查看慢请求**
   ```
   message: "耗时" AND level: "WARN"
   ```

## 应用配置

### 开发环境

应用会自动输出JSON格式日志到 `logs/` 目录,Logstash会定期读取。

### 生产环境

应用会通过TCP直接发送日志到Logstash(端口5044)。

在 `application.yml` 中指定生产环境:
```yaml
spring:
  profiles:
    active: prod
```

## 日志文件结构

```
logs/
├── gateway-server/
│   ├── gateway-server.log          # 文本格式日志
│   └── gateway-server.json.log     # JSON格式日志(用于ELK)
├── system-service/
│   ├── system-service.log
│   └── system-service.json.log
└── todo-service/
    ├── todo-service.log
    └── todo-service.json.log
```

## 维护命令

### 停止ELK服务
```bash
docker-compose down
```

### 停止并删除数据卷
```bash
docker-compose down -v
```

### 查看日志
```bash
docker-compose logs -f elasticsearch
docker-compose logs -f logstash
docker-compose logs -f kibana
```

### 重启服务
```bash
docker-compose restart
```

## 性能优化

### Elasticsearch JVM配置

编辑 `docker-compose.yml`,调整JVM堆内存:
```yaml
environment:
  - "ES_JAVA_OPTS=-Xms2g -Xmx2g"
```

### Logstash管道优化

在 `logstash.conf` 中调整:
```
input {
  tcp {
    port => 5044
    codec => "json"
    type => "application-log"
    threads => 4  # 增加线程数
  }
}
```

## 故障排查

### 1. Elasticsearch无法启动

检查内存是否足够:
```bash
docker-compose logs elasticsearch
```

### 2. Logstash无法连接Elasticsearch

验证Elasticsearch是否正常运行:
```bash
curl http://localhost:9200
```

### 3. Kibana无法连接

检查Elasticsearch连接配置:
```bash
docker-compose logs kibana
```

### 4. 日志未显示在Kibana中

1. 确认日志文件存在: `ls -la logs/*/`
2. 检查Logstash日志: `docker-compose logs logstash`
3. 验证索引是否创建: `curl http://localhost:9200/_cat/indices?v`

## 安全建议

1. **生产环境启用Elasticsearch安全功能**
2. **配置Kibana访问认证**
3. **使用HTTPS加密通信**
4. **定期备份Elasticsearch数据**

## 参考资源

- [Elasticsearch官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)
- [Logstash官方文档](https://www.elastic.co/guide/en/logstash/current/index.html)
- [Kibana官方文档](https://www.elastic.co/guide/en/kibana/current/index.html)
