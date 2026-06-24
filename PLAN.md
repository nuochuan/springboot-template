# 实现计划

## 第一步：明确需求

- 阅读 `REQUIREMENTS.md`。
- 明确本次 MVP。
- 明确输入、输出、边界条件和暂不实现内容。

## 第二步：明确测试

- 阅读 `TEST_CASES.md`。
- 把正常场景、边界场景、异常场景转成自动化测试。
- 先保证核心规则可验证。

## 第三步：设计代码结构

结构：

```text
controller  接收 HTTP 请求，不写复杂业务逻辑
service     核心业务逻辑
common      通用异常处理
model       节点对象和请求/响应对象
```

核心类建议：

- `Node`
- `WeightedRouterService`
- `WeightedRouterController`
- `GlobalExceptionHandler`

算法要求：

- 先过滤出 `enabled = true` 且 `weight > 0` 的节点。
- 对过滤后的节点做加权随机抽取。
- 无可用节点时抛出 `IllegalStateException`。
- 核心逻辑放在 `service`，便于单元测试。

## 第四步：实现 MVP

- 先实现 Service 层核心逻辑。
- 再实现 Controller 层接口。
- 不添加暂不需要的数据库、缓存、MQ、登录等功能。
- 为 Service 编写单元测试，覆盖正常、边界、异常场景。

## 第五步：验证

- 执行 `gradle clean test`。
- 检查测试是否覆盖核心规则。
- 人工 review diff，确认没有无关改动。

测试对应关系：

- 正常场景：权重比例分布测试。
- 边界场景：仅剩一个可用节点时稳定返回该节点。
- 异常场景：`nodes = null`、空列表、过滤后无可用节点时抛异常。

## 第六步：修复问题

如果出现 bug：

1. 先复现。
2. 说明期望结果和实际结果。
3. 分析根因。
4. 做最小修复。
5. 重新运行测试。

## 第七步：交付说明

- 更新 `README.md`。
- 说明如何运行。
- 说明如何测试。
- 说明当前实现内容和限制。
- 准备 Demo 总结。
