# 测试用例

## 测试原则

- 先定义什么叫正确，再写代码。
- 覆盖正常场景、边界场景、异常场景。
- 测试意图必须人工 review，不能只相信自动生成的测试。

## 用例 1：正常场景

输入：

```text
A weight=5 enabled=true
B weight=3 enabled=true
C weight=2 enabled=true
```
调用 `route()` 10000 次

期望输出：

- A、B、C 的选择比例接近 5:3:2。
- 允许随机误差，但三者比例应明显接近权重占比。
- 建议使用固定随机种子或统计阈值做自动化断言，避免测试不稳定。
- 例如 10000 次调用后，可断言 A 在 47%~53%，B 在 27%~33%，C 在 17%~23% 的区间内。

验证点：

- 验证加权路由的核心行为。

## 用例 2：边界场景

输入：

```text
A weight=10 enabled=false
B weight=1 enabled=true
```

期望输出：

- 只会返回 B。
- 连续调用多次后，结果都应为 B。

验证点：

- 验证 `enabled = false` 的节点必须被过滤。


## 用例 3：边界场景

输入：

```text
A weight=0 enabled=true
B weight=5 enabled=true
```

期望输出：

- 只会返回 B。
- 连续调用多次后，结果都应为 B。

验证点：

- 验证 0 权重节点不会被选中。

## 用例 4：异常场景

输入：

```text
A weight=0 enabled=true
B weight=3 enabled=false
```

期望输出：

- 抛出 `IllegalStateException`。
- 异常消息应明确表示没有可用节点。


验证点：

- 验证过滤后无可用节点时必须失败。

## 用例 5：输入异常场景

输入：

```text
nodes = null
```

期望输出：

- 抛出 `IllegalStateException`。

验证点：

- 验证空输入不能被当作有效路由请求。

## 用例 6：输入异常场景

输入：

```text
[]
```

期望输出：

- 抛出 `IllegalStateException`。

验证点：

- 验证空列表不能被路由。
