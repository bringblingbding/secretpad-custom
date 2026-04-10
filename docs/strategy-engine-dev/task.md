# 使用控制策略引擎开发任务看板

- `[x]` Step 1: 精简请求与梳理现存校验，同时新增 `validateAndInject`
  - `[ ]` 修改 `StrategyUsageEngineRequest.java`，仅保留 `projectId`, `userId`, `userRole`, `connectorId`, `actionParams`。
  - `[ ]` 在 `StrategyUsageEngineServiceImpl.java` 中注释掉因此缺失的环境、存储等不可用校验。
  - `[ ]` 新增 `StrategyUsageExecutionResult.java` 返回结果类。
- `[x]` Step 2(重构): 解耦 BasePermission 与 Core引擎
  - `[x]` 2.1 剔除合并状态: 还原 `StrategyUsageEngineRequest` 及 `StrategyUsageEngineServiceImpl` 内部关于 view/download 的嵌套代码，删除不再适配的单测。
  - `[x]` 2.2 新建专属业务模型: 在 `secretpad-service` 新增 `BasePermissionCheckRequest` 以支持独立参数传入。
  - `[x]` 2.3 搭建隔离核销引擎: 迁移判定代码至核心服务 `BasePermissionCheckService` 及其 Impl 类中，依然沿用 `DataUnitConverter`。
  - `[x]` 2.4 新建控制层: 在 `secretpad-web` 创建 `BasePermissionCheckController` 开放 HTTP API。

- `[ ]` Step 3: Redis 环境从零搭建与频率/总量核销
- `[ ]` Step 4: 生命周期的留存与定时销毁
