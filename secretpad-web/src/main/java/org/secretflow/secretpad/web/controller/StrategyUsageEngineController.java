package org.secretflow.secretpad.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.secretflow.secretpad.common.dto.SecretPadResponse;
import org.secretflow.secretpad.service.StrategyUsageEngineService;
import org.secretflow.secretpad.service.model.strategyusageengine.StrategyUsageEngineRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/strategyUsageEngine")
@RequiredArgsConstructor
@Tag(name = "strategy-usage-engine", description = "策略引擎使用控制接口")
public class StrategyUsageEngineController {

    private final StrategyUsageEngineService strategyUsageEngineService;

    /**
     * 对应 Phase 1: 静态校验接口
     * 前端/上游服务在执行敏感操作前，调用此接口进行预检
     */
    @PostMapping("/validate")
    @Operation(summary = "策略静态校验", description = "根据项目、表、用户信息及动作类型，检查是否符合策略约束（Phase 1）")
    public SecretPadResponse<String> validate(@Valid @RequestBody StrategyUsageEngineRequest request) {
        // 调用 Service，如果抛出异常会被全局异常处理器捕获
        strategyUsageEngineService.validate(request);

        // 如果没有抛出异常，说明校验通过
        return SecretPadResponse.success("校验通过，允许执行");
    }
}