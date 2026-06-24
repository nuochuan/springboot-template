# springboot-template

这是一个通用的 Spring Boot + Java 8 + Gradle 项目，用于后端需求。

## 技术栈

- Java 8
- Spring Boot 2.7.18
- Gradle 7.x 推荐
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

## 如何运行测试

如果本机已安装 Gradle：

```bash
gradle clean test
```

如果你想生成 Gradle Wrapper：

```bash
./scripts/create-wrapper.sh
```

然后执行：

```bash
./gradlew clean test
```

## 如何启动应用

```bash
gradle bootRun
```

或者生成 Wrapper 后：

```bash
./gradlew bootRun
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
