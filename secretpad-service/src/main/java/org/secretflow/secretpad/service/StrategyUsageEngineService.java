package org.secretflow.secretpad.service;

import org.secretflow.secretpad.service.model.strategyusageengine.StrategyUsageEngineRequest;
import org.springframework.transaction.annotation.Transactional;

public interface StrategyUsageEngineService {
    @Transactional(readOnly = true)
    /**
     * 执行策略引擎校验
     * 如果校验不通过，将抛出 SecretpadException
     */
    void validate(StrategyUsageEngineRequest request);
}
