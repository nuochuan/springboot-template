# 常用 Prompt

## 1. 让 AI 先审查需求，不写代码

```text
请读取 AGENTS.md、REQUIREMENTS.md、TEST_CASES.md、PLAN.md。
先不要写代码。
请检查这三个任务文件是否足够支持实现 MVP：
1. 需求是否明确。
2. 输入输出是否清楚。
3. 边界条件是否完整。
4. 测试用例是否覆盖正常、边界、异常场景。
直接更改缺失点和修改建议。
```

## 2. 让 AI 实现 MVP

```text
请读取 AGENTS.md、REQUIREMENTS.md、TEST_CASES.md、PLAN.md。
现在实现当前任务的 Spring Boot + Java 8 + Gradle MVP。

要求：
1. 只实现 PLAN.md 中的内容。
2. 不要添加数据库、Redis、MQ、登录、复杂前端。
3. Controller 只负责参数接收和响应包装。
4. Service 负责核心业务逻辑。
5. 使用 JUnit 5 写测试。
6. 实现完成后列出创建和修改的文件。
```

## 3. 让 AI 修 bug

```text
当前有一个 bug。

输入：
...

期望输出：
...

实际输出：
...

错误日志：
...

请先解释根因，不要直接改代码。
然后给出最小修复方案，只修改必要文件。
```

## 4. 让 AI 更新 README

```text
测试已经通过。
请根据当前实现更新 README.md。

需要包含：
1. 实现内容。
2. 如何运行。
3. 如何测试。
4. 核心规则。
5. 验证方式。
6. 当前限制。
7. 后续扩展方向。

不要修改 Java 代码。
```
