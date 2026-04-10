# Step 2.1 基于前端直接按键的权限校验方案设计

既然业务需求变更为**前端页面直接展示“查看”、“复制”、“下载”的显式按钮**，那么策略引擎在这块的职责就从“隐性把关”变成了需要直接响应前端的“权限鉴定接口”和“动作执行接口”。

## 1. 前端页面交互的大致设计

虽然由于生成限制无法给出现成的高保真 UI 图像，但我们可以以极简和现代的设计（类似深色大理石质感或微阴影模式）构建如下前端体验：

### UI 呈现 (Mock)
- **主体内容区**：一张名为“数据资产集”的响应式表格，展示了数据集的名称、体量大小（例如 500 MB）、以及该数据的所在域。
- **右侧操作列**：包含 3 个图标/文字按钮悬浮块：
  - `👁 查看 (View)`：可直接打开一个弹窗预览取样数据。
  - `📋 复制 (Copy)`：将数据子集或全部记录快照拷贝至用户自己的沙箱或结果存盘中。
  - `📥 下载 (Download)`：触发文件流下载，直接保存到本地。
- **动态交互**：如果策略控制器的静态接口返回某权限为 `enabled: false`，则相应的按钮呈现灰色并标记不可点击（或点击后弹窗提示：策略不准入）。

## 2. 后端 REST 接口定义 (Controller设计)

既然前端需要按钮，为了隔离各个操作的行为，我们需要暴露出明确的后台接口，以便让前端与策略引擎 `validateAndInject` 搭起桥梁：

### DTO 入参定义实体
定义通用的前端操作请求体，不用暴露复杂的引擎全量请求，只要这几个必要的定位符：
```java
public class ActionRequestDTO {
    private String projectId;
    private String userId;
... (略，仅传基本维度，不需要提供数量）
}
```

### 推荐 Controller 定义
创建 `StrategyActionController.java`，暴露诸如：
- `POST /api/v1/strategy/action/view`
- `POST /api/v1/strategy/action/copy`
- `POST /api/v1/strategy/action/download`

例如下载接口实现：
```java
@PostMapping("/download")
public R<?> executeDownload(@RequestBody ActionRequestDTO dto) {
    // 1. 构建引擎所需的 context
    StrategyUsageEngineRequest req = new StrategyUsageEngineRequest();
    req.setProjectId(dto.getProjectId());
    // ... 其他属性赋给引擎

    // 2. 关键点：由 Controller 内定 actionType，屏蔽前端的任意篡改可能
    Map<String, Object> params = new HashMap<>();
    params.put("actionType", "download");
    // (如果获取到了被下载文件的已知总 Size，可以一起带入，例如 params.put("requestValue", 50); params.put("requestUnit", "MB"); )
    req.setActionParams(params);

    // 3. 执行策略控制
    StrategyUsageExecutionResult result = strategyUsageEngineService.validateAndInject(req);

    // 4. 全部放行后，开始真正的下载流操作，并将结果返回
    return performRealDownload(dto, result.getInjectedParams());
}
```

## 3. 解析与换算逻辑 (`parseAndCheckBasePermissions` 逻辑修正)

从现存策略的 JSON 可以看出：
`{"maxSingleLimit": {"value": 2000, "unit": "ROWS"}}` 针对的是单次请求的配额控制。
在用户直接点按钮时，对于 `copy` 和 `download` 经常不知道具体有多少行。

### 处理方案：
1. **基础检查 (enabled)**：通过解析对应 json 的 `enabled` 字段决定死活，如果不为 `true` 即刻在报错。
2. **频率/总量阈值检查 (rateLimit & maxTotalCount)**：交由数据库方法 `countByProjectIdAndCheckResult` 等先进行计数验证拦截。
3. **容量单位换算模块介入**：由于您目前的存储上限带有 `MB` 甚至是 `ROWS`。在“点击下载按钮”发起后，若是预知了数据集的大小（比如根据元数据表查出该 `Project` 数据为 `5 MB`）：
   - 将 `5 MB` 和 json 里的配置值送到换算转换层（统一划成 Byte 然后比对数字大小）。若发现超 `maxSingleLimit` (如超过 2000 行)或整体累加总量超 `maxTotalLimit` (超过总流量 500 MB)，抛出 “容量策略受限”并阻断下载。

## 接下来我们怎么做？

如果您觉得新增上述 **Controller 层接口对接思路**，以及 **容量换算预检判定** 合适。下一步我将：
1. 编写单位容量换算器 (`DataUnitConverter.java`)。
2. 帮您补齐并植入这块复杂的 JSON 解析验证（在 `validateAndInject` 方法内部追加 `parseAndCheckBasePermissions` 判断）。
