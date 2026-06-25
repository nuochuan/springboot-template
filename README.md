# springboot-template

这是一个 Spring Boot + Java 8 + Gradle 的年度工资薪金个人所得税计算器 MVP。

## 技术栈

- Java 8
- Spring Boot 2.7.18
- Gradle 8.14.5 Wrapper
- JUnit 5

## 当前实现

- `POST /api/tax/calculate`
- 根据年度工资收入、年度专项扣除、年度其他扣除计算年度个人所得税。
- 核心业务逻辑位于 `TaxCalculationService`。
- Controller 只负责接收请求、调用 Service、包装 `ApiResponse`。
- 使用 DTO 表达请求和响应。
- 复用通用 `ApiResponse`、`BusinessException` 和 `GlobalExceptionHandler`。

## 计算规则

年度基本减除费用固定为 `60000.00` 元。

```text
应纳税所得额 = 年度工资收入 - 60000 - 年度专项扣除 - 年度其他扣除
```

当应纳税所得额小于或等于 0 时：

```text
应纳税所得额 = 0
应纳税额 = 0
税率 = 0
速算扣除数 = 0
```

当应纳税所得额大于 0 时：

```text
应纳税额 = 应纳税所得额 * 适用税率 - 速算扣除数
```

金额计算使用 `BigDecimal`，最终金额保留 2 位小数并使用 `RoundingMode.DOWN`。

## 接口示例

请求：

```bash
curl -X POST http://localhost:8080/api/tax/calculate \
  -H 'Content-Type: application/json' \
  -d '{
    "annualSalaryIncome": 120000,
    "annualSpecialDeduction": 12000,
    "annualOtherDeduction": 8000
  }'
```

响应：

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

## 运行测试

```bash
GRADLE_USER_HOME=.gradle-home ./gradlew test
```

## 启动应用

```bash
GRADLE_USER_HOME=.gradle-home ./gradlew bootRun
```

启动后调用：

```text
POST http://localhost:8080/api/tax/calculate
```

## 当前限制

- 只计算年度工资薪金个人所得税。
- 不实现批量计算。
- 不保存计算历史。
- 不拆分专项附加扣除明细。
- 不计算税收优惠、减免税、补税或退税。
- 不支持多地区、多年度政策配置。
- 不实现月度预扣预缴。
- 不包含数据库、Redis、MQ、配置中心、登录权限或复杂前端。
