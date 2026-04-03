package org.secretflow.secretpad.service;

import org.secretflow.secretpad.service.model.strategyusage.StrategyUsageWithInfoVO;
import org.springframework.transaction.annotation.Transactional;

public interface StrategyUsageService {
    @Transactional(rollbackFor = Exception.class)
    String createStrategy(StrategyUsageWithInfoVO request);

    StrategyUsageWithInfoVO getStrategy(String strategyId);

    @Transactional(rollbackFor = Exception.class)
    void updateStrategy(StrategyUsageWithInfoVO request);

    @Transactional(rollbackFor = Exception.class)
    void deleteStrategy(String strategyId);
}
