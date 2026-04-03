package org.secretflow.secretpad.service.model.strategyusagetemplate;
import lombok.*; // 引入 Lombok
import org.secretflow.secretpad.persistence.entity.StrategyUsageTemplateDO;

@Getter // 生成 get 方法
@Setter // 生成 set 方法
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyUsageTemplatePageRequestVO {

    /**
     * 策略模板信息 (作为查询条件)
     */
    private StrategyUsageTemplateDO strategyUsageTemplateDO;

    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 每页大小
     */
    private Integer pageSize;


}
