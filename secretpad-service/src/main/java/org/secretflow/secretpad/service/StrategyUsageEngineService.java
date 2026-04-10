package org.secretflow.secretpad.service;

import org.secretflow.secretpad.service.model.strategyusageengine.StrategyUsageEngineRequest;
import org.secretflow.secretpad.service.model.strategyusageengine.StrategyUsageExecutionResult;
import org.springframework.transaction.annotation.Transactional;

public interface StrategyUsageEngineService {
    @Transactional(readOnly = true)
    /**
     * 执行策略引擎校验
     * 如果校验不通过，将抛出 SecretpadException
     */
    void validate(StrategyUsageEngineRequest request);

    @Transactional(readOnly = true)
    /**
     * 执行策略引擎校验，并返回安全注入参数字典
     * 如果校验不通过，将抛出 SecretpadException
     */
    StrategyUsageExecutionResult validateAndInject(StrategyUsageEngineRequest request);
}
