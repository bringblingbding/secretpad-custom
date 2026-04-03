package org.secretflow.secretpad.service;

import org.secretflow.secretpad.persistence.entity.StrategyUsageDO;
import org.secretflow.secretpad.persistence.entity.StrategyUsageInfoDO;
import org.secretflow.secretpad.service.model.strategyusageengine.StrategyUsageEngineRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface StrategyUsageDynamicService {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void record(StrategyUsageDO usageDO,
                StrategyUsageInfoDO infoDO,
                StrategyUsageEngineRequest request,
                LocalDateTime operationTime,
                String checkResult,
                String terminatePhase,
                String terminateReason,
                String errorCode);
}
