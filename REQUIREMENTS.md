# 需求说明 Weighted Routing

## 目标

实现一个单机版加权路由 MVP。

## 任务描述

系统维护一组节点，每个节点包含：
- name：节点名称
- weight：节点权重
- enabled：节点是否可用

调用 `route()` 时，系统从可用节点中返回一个节点。

## 接口定义

建议实现一个服务方法：

```text
route(List<Node> nodes) : Node
```

其中 `Node` 至少包含 `name`、`weight`、`enabled` 三个字段。
## MVP 范围

本次只实现以下核心功能：

1. 只允许从 enabled = true 的节点中选择。
2. 只允许从 weight > 0 的节点中选择。
3. 节点被选择的长期比例应该接近它的权重比例。
4. 如果没有任何可用节点，需要抛出明确异常。
5. 本题是概率型路由，不要求每次返回固定结果。

## 输入

输入为节点列表，例如：

```json
[
  {"name": "A", "weight": 5, "enabled": true},
  {"name": "B", "weight": 3, "enabled": true},
  {"name": "C", "weight": 2, "enabled": true}
]
```

## 输出

返回一个被选中的节点，例如：

```json
{"name": "A", "weight": 5, "enabled": true}
```
## 硬约束

- 必须本地可运行。
- 必须有自动化测试。
- 必须处理核心边界情况。
- 必须说明当前假设和限制。

## 业务规则

1. `nodes` 为空、`null`，或者过滤后没有可用节点时，抛出 `IllegalStateException`。
2. 权重小于等于 `0` 的节点不参与路由。
3. `enabled = false` 的节点不参与路由。
4. 路由结果按权重进行概率抽取，累计概率与权重成正比。

## 边界情况

至少考虑：

- 节点列表为空。
- 节点列表为 `null`。
- 所有节点 `enabled = false`。
- 所有节点 `weight <= 0`。
- 部分节点 `enabled = false`。
- 部分节点 `weight <= 0`。
- 节点列表中存在 `null` 元素时应忽略该元素或按无效节点处理，最终仍需遵守“无可用节点抛异常”的规则。
- 正常权重比例场景。

## 暂不实现

本次不做：

1. 登录权限。
2. 数据库存储。
3. Redis / MQ。
4. 复杂前端页面。
5. 生产部署。
6. 复杂监控告警。
