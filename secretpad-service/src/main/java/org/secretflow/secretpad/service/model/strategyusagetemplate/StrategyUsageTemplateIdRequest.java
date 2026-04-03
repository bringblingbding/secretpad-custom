package org.secretflow.secretpad.service.model.strategyusagetemplate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter // 生成 get 方法
@Setter // 生成 set 方法
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyUsageTemplateIdRequest {
    @NotBlank(message = "strategyTemplateId不能为空")
    @Schema(description = "策略模板id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String strategyTemplateId;

}
