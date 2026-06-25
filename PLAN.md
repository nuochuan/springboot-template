# 实现计划

## 第一步：确认 MVP 边界

- 读取 `REQUIREMENTS.md`，确认本次只实现年度工资薪金个人所得税计算器。
- 读取 `TEST_CASES.md`，确认测试覆盖正常计算、税档边界、非法输入、异常路径和重复调用。
- 不实现数据库、Redis、MQ、配置中心、登录权限、复杂前端、批量计算、历史记录、税收优惠、多地区政策配置。
- 技术栈固定为 Spring Boot 2.7.x、Java 8、Gradle。

## 第二步：建模步骤

- 建立税档模型，用于表达：
  - 年度应纳税所得额上限。
  - 税率。
  - 速算扣除数。
- 税档模型只放在内存中，作为 Service 内部常量或私有静态结构。
- 年度基本减除费用固定为 `60000.00`。
- 金额统一使用 `BigDecimal`，不使用 `double` 或 `float`。
- 税档判断规则：
  - 边界值归入较低税档。
  - 超过边界 `0.01` 元进入下一税档。
- 重复请求不建模为业务状态，因为本 MVP 不保存历史数据。

## 第三步：DTO 设计

- 请求 DTO：`TaxCalculateRequest`
  - `annualSalaryIncome: BigDecimal`
  - `annualSpecialDeduction: BigDecimal`
  - `annualOtherDeduction: BigDecimal`
- 响应 DTO：`TaxCalculateResponse`
  - `taxableIncome: BigDecimal`
  - `taxAmount: BigDecimal`
  - `taxRate: BigDecimal`
  - `quickDeduction: BigDecimal`
- 通用响应 DTO：`ApiResponse<T>`
  - `success: boolean`
  - `data: T`
  - `message: String`
- DTO 只表达输入输出结构，不放核心计算逻辑。
- 金额响应统一保留 2 位小数，`taxRate` 保留 2 位小数。

## 第四步：核心 Service 设计

- 创建 `TaxCalculationService`，对外提供 `calculate(TaxCalculateRequest request)`。
- Service 负责全部核心业务逻辑：
  1. 校验请求对象不能为空。
  2. 校验三个金额字段必填。
  3. 校验金额不能小于 0。
  4. 校验金额小数位不能超过 2 位。
  5. 计算应纳税所得额。
  6. 应纳税所得额小于或等于 0 时返回零税额结果。
  7. 应纳税所得额大于 0 时选择税档。
  8. 按 `应纳税所得额 * 税率 - 速算扣除数` 计算应纳税额。
  9. 对最终输出金额执行 `setScale(2, RoundingMode.DOWN)`。
- Service 不依赖 Controller、HTTP、数据库、缓存或外部系统。
- Service 方法应能被纯单元测试直接调用。

## 第五步：Controller 设计

- 创建 `TaxCalculationController`。
- 提供一个 HTTP 接口，例如：

```text
POST /api/tax/calculate
```

- Controller 职责仅限于：
  1. 接收 JSON 请求体。
  2. 调用 `TaxCalculationService`。
  3. 将成功结果包装为 `ApiResponse.success(data)`。
  4. 将业务校验异常交给统一异常处理。
- Controller 不写税档判断、金额计算、字段校验等业务逻辑。

## 第六步：异常处理设计

- 创建业务异常，例如 `BusinessException`，用于表达可预期的校验失败。
- 创建统一异常处理类，例如 `GlobalExceptionHandler`。
- 业务异常返回：

```json
{
  "success": false,
  "message": "明确的第一个校验失败原因"
}
```

- 需要覆盖的错误信息：
  - 空请求体：`request body must not be empty`
  - 缺少或 `null` 字段：`字段名 is required`
  - 负数金额：`字段名 must be greater than or equal to 0`
  - 非数字金额：`字段名 must be a valid number`
  - 小数位超过 2 位：`字段名 scale must be less than or equal to 2`
- 多个字段同时非法时，只返回第一个校验失败原因。
- 非预期异常可以返回通用失败响应，但不暴露堆栈信息。

## 第七步：测试实现步骤

- 优先编写 Service 单元测试，直接验证核心业务规则。
- 再编写 Controller 测试，验证请求响应包装和异常路径。
- 正常场景测试：
  - 适用 3% 税档。
  - 适用 10% 税档。
  - 最高 45% 税档的大金额样例。
- 边界值测试：
  - 应纳税所得额小于 0。
  - 应纳税所得额等于 0。
  - 应纳税所得额等于 `36000`、`144000`、`300000`、`420000`、`660000`、`960000`。
  - 应纳税所得额等于 `36000.01`、`144000.01`、`300000.01`、`420000.01`、`660000.01`、`960000.01`。
- 异常路径测试：
  - 空请求体。
  - 缺少必填字段。
  - 字段为 `null`。
  - 字段为负数。
  - 字段不是合法数字。
  - 字段小数位超过 2 位。
  - 多个字段同时非法时返回第一个错误。
- 重复调用测试：
  - 相同输入连续调用两次，输出保持一致。
- 测试断言必须验证业务结果，不只验证 HTTP 状态码或方法被调用。

## 第八步：本地验证步骤

- 执行自动化测试：

```bash
./gradlew test
```

- 如需本地启动应用：

```bash
./gradlew bootRun
```

- 使用 HTTP 请求人工验证一个正常样例：

```bash
curl -X POST http://localhost:8080/api/tax/calculate \
  -H 'Content-Type: application/json' \
  -d '{
    "annualSalaryIncome": 120000,
    "annualSpecialDeduction": 12000,
    "annualOtherDeduction": 8000
  }'
```

- 期望返回 `taxableIncome = 40000.00`、`taxAmount = 1480.00`、`taxRate = 0.10`、`quickDeduction = 2520.00`。
- 验证通过后人工 review diff，确认没有引入数据库、缓存、MQ、配置中心或复杂架构。

## 第九步：README 更新步骤

- 更新 `README.md`，说明：
  1. 项目实现了年度工资薪金个人所得税计算器 MVP。
  2. 如何运行测试：`./gradlew test`。
  3. 如何本地启动：`./gradlew bootRun`。
  4. 接口路径、请求示例和响应示例。
  5. 核心计算规则和税率表来源于 `REQUIREMENTS.md`。
  6. 当前限制和暂不实现内容。
- README 只描述当前已实现能力，不承诺未实现功能。

## 第十步：交付检查

- 确认核心逻辑位于 Service。
- 确认 Controller 只处理参数接收和响应包装。
- 确认 DTO 用于请求和响应。
- 确认自动化测试覆盖 `TEST_CASES.md` 中的核心用例。
- 确认本地验证命令可执行。
- 确认未添加任务要求之外的依赖和基础设施。
