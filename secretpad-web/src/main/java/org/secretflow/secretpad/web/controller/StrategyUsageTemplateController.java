package org.secretflow.secretpad.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.secretflow.secretpad.persistence.entity.StrategyUsageTemplateDO;
import org.secretflow.secretpad.service.StrategyUsageTemplateService;
import org.secretflow.secretpad.service.model.common.SecretPadResponse;
import org.secretflow.secretpad.service.model.strategyusagetemplate.AllStrategyUsageTemplateListVO;
import org.secretflow.secretpad.service.model.strategyusagetemplate.StrategyUsageTemplatePageRequestVO;
import org.secretflow.secretpad.service.model.strategyusagetemplate.StrategyUsageTemplateIdRequest;
import org.secretflow.secretpad.service.model.strategyusagetemplate.StrategyUsageTemplateWithInfoVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1alpha1/strategyTemplate")
@Tag(name = "strategy-template-controller", description = "策略模板双表【Template 表和 Info 表】联动接口")
@RequiredArgsConstructor
public class StrategyUsageTemplateController {

    private final StrategyUsageTemplateService strategyUsageTemplateService;

    /**
     * 创建策略模板 (同时插入主表和详情表)
     */
    @PostMapping("/create")
    @ResponseBody
    @Operation(summary = "创建完整策略模板", description = "同时向 Template 表和 Info 表插入数据。返回生成的模板ID。")
    public SecretPadResponse<String> create(@RequestBody @Valid StrategyUsageTemplateWithInfoVO request) {
        return SecretPadResponse.success(strategyUsageTemplateService.createStrategyTemplate(request));
    }

    /**
     * 查询策略模板详情
     * 入参示例: { "strategyTemplateId": "uuid-xxx" }
     */
    @PostMapping("/detail")
    @ResponseBody
    @Operation(summary = "查询策略模板详情", description = "根据 strategyTemplateId 联表查询策略模板的关联信息和详细规则。")
    public SecretPadResponse<StrategyUsageTemplateWithInfoVO> detail(@RequestBody @Valid StrategyUsageTemplateIdRequest request) {
        // 参考您的代码习惯，使用 DO 接收只有 ID 的请求
        return SecretPadResponse.success(strategyUsageTemplateService.getStrategyTemplate(request.getStrategyTemplateId()));
    }

    /**
     * 更新策略模板
     * 入参: 必须包含 strategyTemplateId，其他字段可选
     */
    @PostMapping("/update")
    @ResponseBody
    @Operation(summary = "更新策略模板", description = "支持只更新非空字段。根据 strategyTemplateId 同时更新两张表的数据。")
    public SecretPadResponse<Void> update(@RequestBody @Valid StrategyUsageTemplateWithInfoVO request) {
        strategyUsageTemplateService.updateStrategyTemplate(request);
        return SecretPadResponse.success();
    }

    /**
     * 删除策略模板
     * 入参示例: { "strategyTemplateId": "uuid-xxx" }
     */
    @PostMapping("/delete")
    @ResponseBody
    @Operation(summary = "删除策略模板", description = "物理删除。根据 strategyTemplateId 删除模板记录（根据配置是否级联删除 Info）。")
    public SecretPadResponse<Void> delete(@RequestBody @Valid StrategyUsageTemplateIdRequest request) {
        strategyUsageTemplateService.deleteStrategyTemplate(request.getStrategyTemplateId());
        return SecretPadResponse.success();
    }

    /**
     * 分页查询
     * 入参包含分页参数和筛选条件
     */
    @PostMapping("/selectByPage")
    @ResponseBody
    @Operation(summary = "分页查询策略模板", description = "支持分页和条件筛选，返回聚合后的详细信息列表。")
    public SecretPadResponse<AllStrategyUsageTemplateListVO> selectByPage(@RequestBody @Valid StrategyUsageTemplatePageRequestVO request) {
        return SecretPadResponse.success(strategyUsageTemplateService.selectByPage(request));
    }

    /**
     * 列表查询 (无分页)
     * 入参为筛选条件
     */
    @PostMapping("/list")
    @ResponseBody
    @Operation(summary = "列表查询策略模板", description = "根据条件返回所有符合结果的列表（不分页）。")
    public SecretPadResponse<AllStrategyUsageTemplateListVO> list(@RequestBody @Valid StrategyUsageTemplateDO request) {
        return SecretPadResponse.success(strategyUsageTemplateService.list(request));
    }


}