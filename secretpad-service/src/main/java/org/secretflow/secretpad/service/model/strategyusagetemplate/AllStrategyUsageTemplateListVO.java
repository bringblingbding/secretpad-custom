package org.secretflow.secretpad.service.model.strategyusagetemplate;


import lombok.*;
import org.secretflow.secretpad.persistence.entity.StrategyUsageTemplateDO;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllStrategyUsageTemplateListVO {

    /**
     * 策略模板列表,包含所有细节信息
     */
    private List<StrategyUsageTemplateDO> strategyUsageTemplateVOList;

    /**
     * 策略模板数量
     */
     private Integer strategyUsageTemplateNums;


}