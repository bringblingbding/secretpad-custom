# [独立化重构] 基础权限（View, Copy, Download）校验脱钩与重构

您指出的问题非常深刻：`internalDelivery` (联邦建模) 和 `view/copy/download` (数据提取看版) 在底层业务流的**生命周期阶段完全不同**。将它们混合在统一的 `StrategyUsageEngineRequest` 校验中过于冗杂且易致耦合紧密。

遵循您的设计要求，我们将把 2.1 的成果剥离出主引擎，恢复主引擎至精简状态，并为基础权限单独打造一条极简的业务线。

## User Review Required

> [!IMPORTANT]
> 此前注入主引擎测试的 `DataUnitConverter`（容量换算工具）极其好用，虽然代码要分离剥出核心引擎，但我计划保留并复用该工具类。
> 请确认以下包结构与接口设计是否符合您的规范要求。

## Proposed Changes

### 1. 恢复核心使用控制引擎至 2.1 之前

#### [MODIFY] [StrategyUsageEngineRequest.java](file:///d:/develop/projects/secretpad-main_1230/secretpad-service/src/main/java/org/secretflow/secretpad/service/model/strategyusageengine/StrategyUsageEngineRequest.java)
- 删除在此处添加的 `ActionParam` 以及针对多动作的 `List<ActionParam> actionParams`，恢复到只保留 2.1 之前所需的主体约束（userId, role, connectorId）。此请求彻底从冗余动作参数束缚中解脱。

#### [MODIFY] [StrategyUsageEngineServiceImpl.java](file:///d:/develop/projects/secretpad-main_1230/secretpad-service/src/main/java/org/secretflow/secretpad/service/impl/StrategyUsageEngineServiceImpl.java)
- 移除 `checkBasePermissions` 以及 `checkRateAndLimitFromNode` 方法。
- 移除 `validate` 中对其的侵入调用。
- 原策略引擎主要负责把关 `internalDelivery` 和主体信息的纯粹职能。

#### [DELETE] [StrategyUsageBasePermissionTest.java](file:///d:/develop/projects/secretpad-main_1230/secretpad-service/src/test/java/org/secretflow/secretpad/service/impl/StrategyUsageBasePermissionTest.java)
- 将此前的测试类丢弃（后续会重新写针对分离组件的测试）。

---

### 2. 建立专注于 BasePermission (基础权限) 的专属核销体系

#### [NEW] BasePermissionCheckRequest.java
创建于 `service.model.basepermission` 下：
```java
public class BasePermissionCheckRequest {
    private String projectId;
    private String actionType;  // view, copy, download
    private Long requestValue;  // 消耗值
    private String requestUnit; // 消耗单位
}
```

#### [NEW] BasePermissionCheckService.java & Impl
提供专属的 `checkPermission(BasePermissionCheckRequest)` 接口。
- 负责获取 `StrategyUsageInfoDO`。
- 将之前写好的 `checkBasePermissions` （含频率、容量检测以及通过 `DataUnitConverter` 换算的逻辑）全部平移到此类中专职处理。如果查出超额直接往外抛异常阻断。

#### [NEW] BasePermissionCheckController.java
创建于 `secretpad-web`（或者特定的 service 门面下，视贵司架构习惯而定），暴露：
- `POST /api/v1/strategy/permission/check`
前端只需要直接调用该接口请求放行即可，不必像核心业务引擎那样附带环境和协议等一大堆参数。

## Open Questions

> [!WARNING]  
> 这里我假设上述 `BasePermissionCheckController` 对应的 API 层应被放入 `secretpad-web/src/main/java/.../controller` 目录下。请问这个假设是否成立？或者您希望就暂且只落入 Service 层即可？

## Verification Plan

### Automated Tests
1. 重写单元测试 `BasePermissionCheckServiceImplTest`，确保被抽离的代码在新家能够如前文一样阻挡超 `maxSingleLimit` 的越权动作且不被报错。
