# 基础权限动作接口 (BasePermissionAction) 开发完成

我们已经成功完成了 Step 2.5 的开发工作。现在系统支持对受控资产执行“查看、复制、下载”操作，且每个操作都会自动触发策略核销。

## 1. 核心变更概览

### 接口定义 (Controller)
在 `BasePermissionActionController` 中新增了三个功能接口：
- `POST /api/v1alpha1/strategy/permission/action/view`
- `POST /api/v1alpha1/strategy/permission/action/copy`
- `POST /api/v1alpha1/strategy/permission/action/download`

### 自动容量注入逻辑
我们在 `BasePermissionActionServiceImpl` 中实现了一个关键特性：**自动容量预检注入**。
即使前端没有在请求中明确传递 `requestValue` (数据大小)，后端也会在执行 `check()` 之前，根据 `nodeId` 和 `dataId` 自动从物理存储中获取文件实际大小，并将其注入到校验上下文中。这确保了 `maxSingleLimit` (单次下载量上限) 等策略能够被严格执行。

## 2. 功能详情

| 功能 | 实现逻辑 | 说明 |
| :--- | :--- | :--- |
| **查看 (View)** | 读取 CSV 数据的前 100 行采样 | 快速预览，不消耗大额流量 |
| **复制 (Copy)** | 读取全文数据文本并返回 | 用于前端展示供用户手动复制 |
| **下载 (Download)** | 复用 `DataService` 下载流 | 触发浏览器文件保存，严控容量上限 |

## 3. 已完成的文件列表

- `BasePermissionActionController.java`: 暴露动作 API。
- `BasePermissionActionService.java` & `Impl`: 实现采样、读取与核销注入。
- `BasePermissionCheckRequest.java`: 已扩展 `nodeId` 和 `domainDataId` 坐标字段。

## 4. 验证建议

您可以使用 Postman 或前端调用 `/view` 接口。如果该项目配置了 `view` 策略为 `enabled: false`，后端将抛出 `StrategyCheckFailed` 错误，且不会执行任何磁盘 IO。

---
> [!TIP]
> 目前所有更改已提交至 GitHub：[secretpad-custom](https://github.com/bringblingbding/secretpad-custom)
