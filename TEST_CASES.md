# 测试用例

## 测试原则

- 先定义什么叫正确，再写代码。
- 覆盖正常场景、边界场景、异常场景。
- 测试意图必须人工 review，不能只相信自动生成的测试。

## 用例 1：正常场景

输入：

```json
{ "name": "Noah" }
```

期望输出：

```json
{
  "success": true,
  "data": { "message": "Hello, Noah" },
  "message": "success"
}
```

验证点：

- 核心功能可以正常完成。

## 用例 2：边界场景

输入：

```json
{ "name": "  Noah  " }
```

期望输出：

```json
{
  "success": true,
  "data": { "message": "Hello, Noah" },
  "message": "success"
}
```

验证点：

- 前后空白会被正确处理。

## 用例 3：异常场景

输入：

```json
{ "name": " " }
```

期望输出：

```json
{
  "success": false,
  "message": "name must not be blank"
}
```

验证点：

- 非法输入不会导致未知异常。

## 用例 4：业务陷阱

输入：

```json
{}
```

期望输出：

```json
{
  "success": false,
  "message": "name must not be blank"
}
```

验证点：

- 缺失参数和空白参数走同一条业务错误路径。
