# springboot-template

这是一个用于后端需求交付的 Spring Boot + Java 8 + Gradle 示例项目。

## 技术栈

- Java 8
- Spring Boot 2.7.18
- Gradle 8.14.5 Wrapper
- JUnit 5

## 项目结构

```text
springboot-template/
├── AGENTS.md
├── REQUIREMENTS.md
├── TEST_CASES.md
├── PLAN.md
├── README.md
├── build.gradle
├── settings.gradle
├── gradle.properties
├── docs/
├── templates/
├── scripts/
└── src/
```
## 当前实现

- `GET /api/demo/hello?name=Noah`
- `name` 会先 trim，再拼接成 `Hello, Noah`
- 统一使用 `ApiResponse` 返回
- 空值、纯空白、缺失参数会返回明确业务错误

## 运行测试

```bash
GRADLE_USER_HOME=.gradle-home ./gradlew test
```

如果本机已有可用 Gradle，也可以直接运行：

```bash
gradle test
```

## 启动应用

```bash
GRADLE_USER_HOME=.gradle-home ./gradlew bootRun
```

启动后访问：

```text
GET http://localhost:8080/api/demo/hello?name=Noah
```

## 每次新任务怎么用

1. 修改 `REQUIREMENTS.md`，写清楚目标、输入、输出、边界和暂不实现内容。
2. 修改 `TEST_CASES.md`，写清楚正常、边界、异常、业务陷阱测试。
3. 修改 `PLAN.md`，拆分实现步骤。
4. 让 AI 工具读取 `AGENTS.md`、`REQUIREMENTS.md`、`TEST_CASES.md`、`PLAN.md`。
5. 只实现 MVP。
6. 执行测试。
7. 人工 review diff。
8. 修 bug 时先复现、再根因、再最小修复。
9. 更新 `README.md`。

## 当前模板包含

- 通用 Spring Boot 启动类。
- 通用响应结构。
- 通用业务异常。
- 全局异常处理。
- Demo Controller。
- Demo Service。
- Service 单元测试。
- Controller Web 测试。
- 中文需求、测试、计划模板。

## 当前模板不包含

- 数据库。
- Redis。
- MQ。
- 登录权限。
- 前端页面。
- 复杂监控。

这些内容只有在任务明确需要时才添加。
