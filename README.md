# springboot-template

这是一个基于 Spring Boot 2.7.18、Java 8 和 Gradle 的模板项目。当前已实现一个单机版加权路由 MVP，用于根据节点权重从可用节点中随机选出一个节点。

## 1. 本项目实现了什么

当前实现包括：

- `Node`：路由节点模型，包含 `name`、`weight`、`enabled`。
- `WeightedRouterService`：核心加权路由逻辑。
- `WeightedRouterController`：提供 HTTP 接口接收节点列表并返回路由结果。
- 全局异常处理：将无可用节点等业务异常统一返回为 HTTP 400。

## 2. 如何运行测试

使用 Gradle Wrapper：

```bash
./gradlew clean test
```

如果本机安装了 Gradle，也可以直接执行：

```bash
gradle clean test
```

## 3. 如何启动服务

使用 Gradle Wrapper：

```bash
./gradlew bootRun
```

或者使用本机 Gradle：

```bash
gradle bootRun
```

服务默认启动在 `http://localhost:8080`。

## 4. API 示例

### 路由接口

- 方法：`POST`
- 路径：`/api/router/route`
- 请求体：节点列表 JSON

请求示例：

```bash
curl -X POST "http://localhost:8080/api/router/route" \
  -H "Content-Type: application/json" \
  -d '[
    {"name":"A","weight":5,"enabled":true},
    {"name":"B","weight":3,"enabled":true},
    {"name":"C","weight":2,"enabled":true}
  ]'
```

响应示例：

```json
{
  "success": true,
  "data": {
    "name": "A",
    "weight": 5,
    "enabled": true
  },
  "message": "success"
}
```

## 5. 核心规则

- 只从 `enabled = true` 的节点中选择。
- 只从 `weight > 0` 的节点中选择。
- 按权重比例进行概率抽取。
- `nodes` 为 `null`、空列表，或过滤后没有可用节点时，抛出 `IllegalStateException`。
- `null` 节点元素会被忽略。
- 本题是概率型路由，不保证每次返回固定结果。

## 6. 验证方式

测试覆盖了以下场景：

- 正常场景：`A:5, B:3, C:2` 的概率分布落在容忍区间内。
- 边界场景：只剩一个可用节点时，结果稳定返回该节点。
- 异常场景：`nodes = null`、空列表、过滤后无可用节点时抛出 `IllegalStateException`。
- Web 场景：Controller 返回标准 `ApiResponse`，并在异常时返回 400。

## 7. 当前限制

- 仅实现单机版路由，没有分布式一致性能力。
- 没有持久化、缓存、消息队列、登录、权限或复杂前端。
- 路由是随机概率型，不是确定性轮询。
- 当前接口对节点 `name` 不做额外校验。

## 8. 后续扩展方向

- 增加更多路由策略，例如加权轮询、平滑加权轮询。
- 增加节点健康检查和动态上下线能力。
- 增加管理接口，支持动态更新节点配置。
- 增加更细的指标和日志，便于观察路由结果分布。
- 如任务需要，再接入数据库、缓存或配置中心。

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
├── src/
└── ...
```
