package org.secretflow.secretpad.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.secretflow.secretpad.common.errorcode.StrategyUsageControlErrorCode;
import org.secretflow.secretpad.common.exception.SecretpadException;
import org.secretflow.secretpad.persistence.entity.StrategyUsageDO;
import org.secretflow.secretpad.persistence.entity.StrategyUsageInfoDO;
import org.secretflow.secretpad.persistence.repository.StrategyUsageDynamicRepository;
import org.secretflow.secretpad.persistence.repository.StrategyUsageInfoRepository;
import org.secretflow.secretpad.persistence.repository.StrategyUsageRepository;
import org.secretflow.secretpad.service.BasePermissionCheckService;
import org.secretflow.secretpad.service.model.basepermission.BasePermissionCheckRequest;
import org.secretflow.secretpad.service.util.DataUnitConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 基础权限校验服务实现
 * 独立于核心策略引擎，专门负责 view/copy/download 三类操作的策略合规检测
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BasePermissionCheckServiceImpl implements BasePermissionCheckService {

    private final StrategyUsageRepository usageRepository;
    private final StrategyUsageInfoRepository infoRepository;
    private final StrategyUsageDynamicRepository dynamicRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void check(BasePermissionCheckRequest request) {
        log.info("Base permission check: projectId={}, userId={}", request.getProjectId(), request.getUserId());

        // 1. 获取策略配置
        StrategyUsageDO usageDO = usageRepository.findByProjectId(request.getProjectId())
                .orElseThrow(() -> SecretpadException.of(
                        StrategyUsageControlErrorCode.STRATEGY_NOT_FOUND,
                        String.format("Project %s has no strategy", request.getProjectId())
                ));

        StrategyUsageInfoDO infoDO = infoRepository.findById(usageDO.getInfoId())
                .orElseThrow(() -> SecretpadException.of(
                        StrategyUsageControlErrorCode.STRATEGY_NOT_FOUND,
                        "Strategy Info ID: " + usageDO.getInfoId()
                ));

        // 2. 用户与角色校验
        if (StringUtils.isNotBlank(infoDO.getAllowedUsers())
                && !checkCsvContains(infoDO.getAllowedUsers(), request.getUserId())) {
            throwError(String.format("user [%s] is not allowed. permitted: [%s]",
                    request.getUserId(), infoDO.getAllowedUsers()));
        }
        if (StringUtils.isNotBlank(infoDO.getRoleLimit())
                && !checkCsvContains(infoDO.getRoleLimit(), request.getUserRole())) {
            throwError(String.format("role [%s] is not allowed. permitted: [%s]",
                    request.getUserRole(), infoDO.getRoleLimit()));
        }

        // 3. 逐个校验动作列表
        if (request.getActions() == null || request.getActions().isEmpty()) {
            throwError("actions list cannot be empty");
        }

        for (BasePermissionCheckRequest.ActionItem action : request.getActions()) {
            checkSingleAction(infoDO, action, request.getProjectId());
        }

        log.info("Base permission check passed for project={}", request.getProjectId());
    }

    /**
     * 对单个动作执行完整校验链
     */
    private void checkSingleAction(StrategyUsageInfoDO infoDO, BasePermissionCheckRequest.ActionItem action, String projectId) {
        if (StringUtils.isBlank(action.getActionType())) {
            throwError("actionType cannot be blank");
        }

        String actionType = action.getActionType().toLowerCase();
        String jsonConfig = getJsonConfigByAction(infoDO, actionType);

        if (StringUtils.isBlank(jsonConfig)) {
            log.warn("Strategy {} has no config for action [{}]", infoDO.getInfoId(), actionType);
            throwError(String.format("action [%s] has no strategy config", actionType));
        }

        try {
            JsonNode node = objectMapper.readTree(jsonConfig);

            // A. 是否启用
            if (node.has("enabled") && !node.get("enabled").asBoolean()) {
                throwError(String.format("action [%s] is disabled by strategy", actionType));
            }

            // B. 单次上限校验 (maxSingleLimit)
            checkSingleLimit(node, action, actionType);

            // C. 累计次数校验 (maxTotalCount) 与频率限制 (rateLimit)
            checkRateAndLimit(node, projectId, actionType);

            // D. TODO: maxTotalLimit 总数据量累加校验，需配合 Redis 后续实现

        } catch (SecretpadException se) {
            throw se;
        } catch (Exception e) {
            log.error("Parse permission JSON error for action [{}]", actionType, e);
            throwError(String.format("strategy config for [%s] is invalid", actionType));
        }
    }

    /**
     * 根据动作类型提取对应的 JSON 配置字符串
     */
    private String getJsonConfigByAction(StrategyUsageInfoDO infoDO, String actionType) {
        switch (actionType) {
            case "view":
                return infoDO.getView();
            case "copy":
                return infoDO.getCopy();
            case "download":
                return infoDO.getDownload();
            default:
                throwError(String.format("unknown actionType [%s], only view/copy/download are supported", actionType));
                return null;
        }
    }

    /**
     * 校验单次请求量是否超过 maxSingleLimit
     */
    private void checkSingleLimit(JsonNode node, BasePermissionCheckRequest.ActionItem action, String actionType) {
        if (!node.has("maxSingleLimit")) {
            return;
        }
        JsonNode limitObj = node.get("maxSingleLimit");
        if (!limitObj.has("value") || !limitObj.has("unit")) {
            return;
        }

        long limitValue = limitObj.get("value").asLong();
        String limitUnit = limitObj.get("unit").asText();

        // 如果调用方没有传 requestValue，跳过容量校验（仅校验 enabled 和频率）
        if (action.getRequestValue() == null || StringUtils.isBlank(action.getRequestUnit())) {
            return;
        }

        long requestValue = action.getRequestValue();
        String requestUnit = action.getRequestUnit();

        if (!DataUnitConverter.isSameSystem(requestUnit, limitUnit)) {
            throwError(String.format("unit mismatch for [%s]: limit is %s, request is %s",
                    actionType, limitUnit, requestUnit));
        }

        long reqConverted = DataUnitConverter.convertToBytesIfCapacity(requestValue, requestUnit);
        long limitConverted = DataUnitConverter.convertToBytesIfCapacity(limitValue, limitUnit);

        if (reqConverted > limitConverted) {
            throwError(String.format("single limit exceeded for [%s]: request %d %s > limit %d %s",
                    actionType, requestValue, requestUnit, limitValue, limitUnit));
        }
    }

    /**
     * 校验累计次数和频率窗口
     */
    private void checkRateAndLimit(JsonNode node, String projectId, String actionType) {
        if (node.has("maxTotalCount")) {
            long maxTotal = node.get("maxTotalCount").asLong();
            long actualTotal = dynamicRepository.countByProjectIdAndCheckResult(projectId, "PASS");
            if (actualTotal >= maxTotal) {
                throwError(String.format("[%s] total count [%d] reaches limit [%d]",
                        actionType, actualTotal, maxTotal));
            }
        }

        if (node.has("rateLimit")) {
            JsonNode rate = node.get("rateLimit");
            int rateCount = rate.get("count").asInt();
            String timeUnit = rate.get("timeUnit").asText();

            LocalDateTime windowStart = calculateWindowStart(timeUnit);
            long recentCount = dynamicRepository.countByProjectIdAndCheckResultAndCurrentOperationTimeAfter(
                    projectId, "PASS", windowStart);

            if (recentCount >= rateCount) {
                throwError(String.format("[%s] rate limit exceeded: [%d] requests in last [%s], limit is [%d]",
                        actionType, recentCount, timeUnit, rateCount));
            }
        }
    }

    private LocalDateTime calculateWindowStart(String unit) {
        if (StringUtils.isBlank(unit)) {
            return LocalDateTime.now().minusMinutes(1);
        }
        LocalDateTime now = LocalDateTime.now();
        switch (unit.toUpperCase()) {
            case "SECONDS": return now.minusSeconds(1);
            case "MINUTES": return now.minusMinutes(1);
            case "HOURS":   return now.minusHours(1);
            case "DAYS":    return now.minusDays(1);
            default:
                log.warn("Unknown timeUnit: {}, fallback to MINUTES", unit);
                return now.minusMinutes(1);
        }
    }

    private boolean checkCsvContains(String csvList, String target) {
        if (csvList == null || target == null) return false;
        for (String item : csvList.split(",")) {
            if (item.trim().equalsIgnoreCase(target)) return true;
        }
        return false;
    }

    private void throwError(String detail) {
        throw SecretpadException.of(StrategyUsageControlErrorCode.STRATEGY_CHECK_FAILED, detail);
    }
}
