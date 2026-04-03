package org.secretflow.secretpad.service;

import org.secretflow.secretpad.persistence.entity.StrategyUsageTemplateDO;
import org.secretflow.secretpad.service.model.strategyusagetemplate.AllStrategyUsageTemplateListVO;
import org.secretflow.secretpad.service.model.strategyusagetemplate.StrategyUsageTemplatePageRequestVO;
import org.secretflow.secretpad.service.model.strategyusagetemplate.StrategyUsageTemplateWithInfoVO;
import org.springframework.transaction.annotation.Transactional;

public interface StrategyUsageTemplateService {
    @Transactional(rollbackFor = Exception.class)
    String createStrategyTemplate(StrategyUsageTemplateWithInfoVO request);

    @Transactional(rollbackFor = Exception.class)
    StrategyUsageTemplateWithInfoVO getStrategyTemplate(String strategyTemplateId);

    @Transactional(rollbackFor = Exception.class)
    void updateStrategyTemplate(StrategyUsageTemplateWithInfoVO request);

    @Transactional(rollbackFor = Exception.class)
    void deleteStrategyTemplate(String strategyTemplateId);

    //写一个/selectByPage方法，返回分页结果
    @Transactional(rollbackFor = Exception.class)
    AllStrategyUsageTemplateListVO selectByPage(StrategyUsageTemplatePageRequestVO request);

    @Transactional(rollbackFor = Exception.class)
    AllStrategyUsageTemplateListVO list(StrategyUsageTemplateDO request);

}
