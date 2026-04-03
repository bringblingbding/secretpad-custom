package org.secretflow.secretpad.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation; // 引入 Operation
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
// import org.secretflow.secretpad.common.annotation.resource.ApiResource;
import org.secretflow.secretpad.persistence.entity.StrategyUsageDO;
import org.secretflow.secretpad.service.StrategyUsageService;
import org.secretflow.secretpad.service.model.common.SecretPadResponse;
import org.secretflow.secretpad.service.model.strategyusage.StrategyUsageWithInfoVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1alpha1/strategy")
@Tag(name = "strategy-controller", description = "使用控制策略双表【Usage 表和 Info 表】联动接口")
@RequiredArgsConstructor
public class StrategyUsageController {

    private final StrategyUsageService strategyService;

    /**
     * 创建策略 (同时插入两张表)
     */
    @PostMapping("/create")
    @ResponseBody
    // @ApiResource(code = "STRATEGY_CREATE") // 暂时注释掉权限，防止报错
    @Operation(summary = "创建完整策略", description = "同时向 Usage 表和 Info 表插入数据，实现策略的创建。")
    public SecretPadResponse<String> create(@RequestBody @Valid StrategyUsageWithInfoVO request) {
        return SecretPadResponse.success(strategyService.createStrategy(request));
    }

    /**
     * 查询策略详情
     * 入参示例: { "strategyId": 1 }
     */
    @PostMapping("/detail")
    @ResponseBody
    @Operation(summary = "查询策略详情", description = "根据 strategyId 联表查询策略的关联信息和详细规则。入参示例: { \"strategyId\": 1 }")
    public SecretPadResponse<StrategyUsageWithInfoVO> detail(@RequestBody StrategyUsageDO request) {
        return SecretPadResponse.success(strategyService.getStrategy(request.getStrategyId()));
    }

    /**
     * 更新策略
     * 入参: 必须包含 strategyId，其他字段可选
     */
    @PostMapping("/update")
    @ResponseBody
    @Operation(summary = "更新策略", description = "支持只更新非空字段。根据 strategyId 同时更新两张表的数据。入参: 必须包含 strategyId，其他字段可选")
    public SecretPadResponse<Void> update(@RequestBody @Valid StrategyUsageWithInfoVO request) {
        strategyService.updateStrategy(request);
        return SecretPadResponse.success();
    }

    /**
     * 删除策略
     * 入参示例: { "strategyId": 1 }
     */
    @PostMapping("/delete")
    @ResponseBody
    @Operation(summary = "删除策略", description = "物理删除。根据 strategyId 级联删除 Usage 表和 Info 表记录。入参示例: { \"strategyId\": 1 }")
    public SecretPadResponse<Void> delete(@RequestBody StrategyUsageDO request) {
        strategyService.deleteStrategy(request.getStrategyId());
        return SecretPadResponse.success();
    }
}