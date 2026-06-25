# 测试用例

## 测试原则

- 测试必须验证个税业务规则，而不是只验证代码分支是否执行。
- 金额期望值必须能按 `REQUIREMENTS.md` 中的公式人工复算。
- 成功响应必须验证 `taxableIncome`、`taxAmount`、`taxRate`、`quickDeduction`。
- 失败响应必须验证 `success = false` 和明确的错误信息。
- 金额字段按字符串展示期望值，避免 JSON 数字精度或格式影响人工校验。

## 统一响应格式约定

成功响应：

```json
{
  "success": true,
  "data": {
    "taxableIncome": "40000.00",
    "taxAmount": "1480.00",
    "taxRate": "0.10",
    "quickDeduction": "2520.00"
  },
  "message": "success"
}
```

失败响应：

```json
{
  "success": false,
  "message": "明确的第一个校验失败原因"
}
```

## 用例 1：正常场景 - 适用 10% 税档

输入：

```json
{
  "annualSalaryIncome": 120000,
  "annualSpecialDeduction": 12000,
  "annualOtherDeduction": 8000
}
```

人工校验：

```text
应纳税所得额 = 120000 - 60000 - 12000 - 8000 = 40000
应纳税额 = 40000 * 10% - 2520 = 1480
```

期望输出：

```json
{
  "success": true,
  "data": {
    "taxableIncome": "40000.00",
    "taxAmount": "1480.00",
    "taxRate": "0.10",
    "quickDeduction": "2520.00"
  },
  "message": "success"
}
```

验证点：

- 正常金额可以完成计算。
- 会先扣除年度基本减除费用 60000 元。
- 应纳税所得额超过 36000 后使用 10% 税档和 2520 速算扣除数。

## 用例 2：正常场景 - 适用 3% 税档

输入：

```json
{
  "annualSalaryIncome": 90000,
  "annualSpecialDeduction": 0,
  "annualOtherDeduction": 0
}
```

人工校验：

```text
应纳税所得额 = 90000 - 60000 = 30000
应纳税额 = 30000 * 3% - 0 = 900
```

期望输出：

```json
{
  "success": true,
  "data": {
    "taxableIncome": "30000.00",
    "taxAmount": "900.00",
    "taxRate": "0.03",
    "quickDeduction": "0.00"
  },
  "message": "success"
}
```

验证点：

- 输入金额为 0 时可以正常参与计算。
- 应纳税所得额不超过 36000 时使用 3% 税档。

## 用例 3：应纳税所得额等于 0

输入：

```json
{
  "annualSalaryIncome": 60000,
  "annualSpecialDeduction": 0,
  "annualOtherDeduction": 0
}
```

期望输出：

```json
{
  "success": true,
  "data": {
    "taxableIncome": "0.00",
    "taxAmount": "0.00",
    "taxRate": "0.00",
    "quickDeduction": "0.00"
  },
  "message": "success"
}
```

验证点：

- 应纳税所得额等于 0 时不产生税额。

## 用例 4：应纳税所得额小于 0

输入：

```json
{
  "annualSalaryIncome": 50000,
  "annualSpecialDeduction": 10000,
  "annualOtherDeduction": 0
}
```

期望输出：

```json
{
  "success": true,
  "data": {
    "taxableIncome": "0.00",
    "taxAmount": "0.00",
    "taxRate": "0.00",
    "quickDeduction": "0.00"
  },
  "message": "success"
}
```

验证点：

- 应纳税所得额小于 0 时按 0 处理。

## 用例 5：税档边界值

输入和期望：

| 应纳税所得额 | 输入 annualSalaryIncome | 期望税率 | 期望速算扣除数 | 期望应纳税额 |
| --- | --- | --- | --- | --- |
| 36000.00 | 96000.00 | 0.03 | 0.00 | 1080.00 |
| 144000.00 | 204000.00 | 0.10 | 2520.00 | 11880.00 |
| 300000.00 | 360000.00 | 0.20 | 16920.00 | 43080.00 |
| 420000.00 | 480000.00 | 0.25 | 31920.00 | 73080.00 |
| 660000.00 | 720000.00 | 0.30 | 52920.00 | 145080.00 |
| 960000.00 | 1020000.00 | 0.35 | 85920.00 | 250080.00 |

统一输入规则：

```json
{
  "annualSalaryIncome": "见表格",
  "annualSpecialDeduction": 0,
  "annualOtherDeduction": 0
}
```

验证点：

- 税档边界值包含在较低税档内。
- 每个税档边界都能按税率表人工复算。

## 用例 6：刚超过税档边界值

输入和期望：

| 应纳税所得额 | 输入 annualSalaryIncome | 期望税率 | 期望速算扣除数 | 期望应纳税额 |
| --- | --- | --- | --- | --- |
| 36000.01 | 96000.01 | 0.10 | 2520.00 | 1080.00 |
| 144000.01 | 204000.01 | 0.20 | 16920.00 | 11880.00 |
| 300000.01 | 360000.01 | 0.25 | 31920.00 | 43080.00 |
| 420000.01 | 480000.01 | 0.30 | 52920.00 | 73080.00 |
| 660000.01 | 720000.01 | 0.35 | 85920.00 | 145080.00 |
| 960000.01 | 1020000.01 | 0.45 | 181920.00 | 250080.00 |

统一输入规则：

```json
{
  "annualSalaryIncome": "见表格",
  "annualSpecialDeduction": 0,
  "annualOtherDeduction": 0
}
```

验证点：

- 超过边界 0.01 元时进入下一税档。
- 应纳税额按 `RoundingMode.DOWN` 保留 2 位小数。

## 用例 7：极大金额输入

输入：

```json
{
  "annualSalaryIncome": 2000000,
  "annualSpecialDeduction": 0,
  "annualOtherDeduction": 0
}
```

人工校验：

```text
应纳税所得额 = 2000000 - 60000 = 1940000
应纳税额 = 1940000 * 45% - 181920 = 691080
```

期望输出：

```json
{
  "success": true,
  "data": {
    "taxableIncome": "1940000.00",
    "taxAmount": "691080.00",
    "taxRate": "0.45",
    "quickDeduction": "181920.00"
  },
  "message": "success"
}
```

验证点：

- 极大金额不会溢出。
- 最高税档计算结果可人工校验。

## 用例 8：空请求体

输入：

```json
null
```

期望输出：

```json
{
  "success": false,
  "message": "request body must not be empty"
}
```

验证点：

- 空输入不会导致未知异常。

## 用例 9：缺少必填字段

输入：

```json
{
  "annualSpecialDeduction": 12000,
  "annualOtherDeduction": 8000
}
```

期望输出：

```json
{
  "success": false,
  "message": "annualSalaryIncome is required"
}
```

验证点：

- 缺少必填字段时返回明确错误。
- 只需要返回第一个校验失败原因。

## 用例 10：字段为 null

输入：

```json
{
  "annualSalaryIncome": 120000,
  "annualSpecialDeduction": null,
  "annualOtherDeduction": 8000
}
```

期望输出：

```json
{
  "success": false,
  "message": "annualSpecialDeduction is required"
}
```

验证点：

- 字段为 `null` 与缺少字段一样属于必填校验失败。

## 用例 11：负数金额

输入：

```json
{
  "annualSalaryIncome": -1,
  "annualSpecialDeduction": 0,
  "annualOtherDeduction": 0
}
```

期望输出：

```json
{
  "success": false,
  "message": "annualSalaryIncome must be greater than or equal to 0"
}
```

验证点：

- 任一金额字段不能小于 0。

## 用例 12：非数字金额

输入：

```json
{
  "annualSalaryIncome": "abc",
  "annualSpecialDeduction": 0,
  "annualOtherDeduction": 0
}
```

期望输出：

```json
{
  "success": false,
  "message": "annualSalaryIncome must be a valid number"
}
```

验证点：

- 非法数字格式不会导致未知异常。

## 用例 13：小数位超过 2 位

输入：

```json
{
  "annualSalaryIncome": 100000.001,
  "annualSpecialDeduction": 0,
  "annualOtherDeduction": 0
}
```

期望输出：

```json
{
  "success": false,
  "message": "annualSalaryIncome scale must be less than or equal to 2"
}
```

验证点：

- 输入金额超过 2 位小数时按非法输入处理，而不是自动四舍五入或截断。

## 用例 14：多个字段同时非法

输入：

```json
{
  "annualSalaryIncome": null,
  "annualSpecialDeduction": -1,
  "annualOtherDeduction": 0
}
```

期望输出：

```json
{
  "success": false,
  "message": "annualSalaryIncome is required"
}
```

验证点：

- 多个字段同时非法时只返回第一个校验失败原因。

## 用例 15：相同输入重复调用

输入：

```json
{
  "annualSalaryIncome": 120000,
  "annualSpecialDeduction": 12000,
  "annualOtherDeduction": 8000
}
```

期望输出：

连续调用两次，响应内容均与“用例 1”完全一致。

验证点：

- 本 MVP 不保存历史记录。
- 相同输入重复调用时返回相同结果。
