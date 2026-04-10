package org.secretflow.secretpad.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.secretflow.secretpad.common.util.JsonUtils;
import org.secretflow.secretpad.common.util.UUIDUtils;
import org.secretflow.secretpad.persistence.entity.StrategyUsageDO;
import org.secretflow.secretpad.persistence.entity.StrategyUsageDynamicDO;
import org.secretflow.secretpad.persistence.entity.StrategyUsageInfoDO;
import org.secretflow.secretpad.persistence.repository.StrategyUsageDynamicRepository;
import org.secretflow.secretpad.service.StrategyUsageDynamicService;
import org.secretflow.secretpad.service.model.strategyusageengine.StrategyUsageEngineRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StrategyUsageDynamicServiceImpl implements StrategyUsageDynamicService {

    private final StrategyUsageDynamicRepository strategyUsageDynamicRepository;
    /**
     * 使用 REQUIRES_NEW 传播机制，确保即使外部业务事务回滚（比如校验失败抛异常），
     * 这条日志记录依然能成功存入数据库。
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(StrategyUsageDO usageDO,
                       StrategyUsageInfoDO infoDO,
                       StrategyUsageEngineRequest request,
                       LocalDateTime operationTime,
                       String checkResult,
                       String terminatePhase,
                       String terminateReason,
                       String errorCode) {
        try {
            String projectId = request.getProjectId();
            String strategyId = usageDO == null ? null : usageDO.getStrategyId();

            // 1. 获取当前项目的累积使用次数 (Count + 1)
            long currentCount = strategyUsageDynamicRepository.countByProjectId(projectId) + 1;
            // 2. 生成 UUID (去掉横杠使字符串更整洁)
            String uuid = UUIDUtils.random(8);

            // 2. 构建 DO 对象 (直接映射到数据库)
            StrategyUsageDynamicDO dynamicDO = StrategyUsageDynamicDO.builder()
                    .usageId(uuid)
                    .strategyId(strategyId)
                    .projectId(projectId)
                    .usageCount((int) currentCount)
                    .currentOperationTime(operationTime)
                    .users(request.getUserId())
                    .usageConnectors(request.getConnectorId())
                    .role(request.getUserRole())

                    // 结果与异常
                    .checkResult(checkResult)
                    .terminatePhase(terminatePhase)
                    .terminateReason(terminateReason)
                    .errorCode(errorCode)
                    .build();
            // 设置基类字段
            dynamicDO.setIsDeleted(false);
            dynamicDO.setGmtCreate(operationTime);
            dynamicDO.setGmtModified(operationTime);


            // 3. 执行插入 (使用 Repository 中定义的 native insert 或 save)
            strategyUsageDynamicRepository.save(dynamicDO);

            log.info("Successfully recorded strategy usage dynamic log for project: {}, count: {}",
                    projectId, currentCount);

        } catch (Exception e) {
            log.error("record strategy usage dynamic log failed, projectId={}",
                    request.getProjectId(), e);
            // 注意：此处不抛出异常，防止记录日志失败导致主业务流程（如计算任务）中断
        }
    }
}