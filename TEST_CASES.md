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
调用 route() 10000 次

期望输出：

A、B、C 的选择比例大致接近 5:3:2
允许一定随机误差

验证点：

- 验证加权路由的核心行为。

## 用例 2：边界场景

输入：

```text
A weight=10 enabled=false
B weight=1 enabled=true
```

期望输出：

route() 永远返回 B


验证点：

- 验证不可用节点必须被过滤。


## 用例 3：异常场景

输入：

```text
A weight=0 enabled=true
B weight=5 enabled=true
```

期望输出：

route() 永远返回 B

- 返回明确错误，或者抛出明确异常。

验证点：

- 验证 0 权重节点不会被选中。

## 用例 4：业务陷阱

输入：

```text
A weight=0 enabled=true
B weight=3 enabled=false
```

期望输出：

抛出 IllegalStateException


验证点：

- 异常边界。
