package org.secretflow.secretpad.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
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
import org.secretflow.secretpad.service.model.strategyusageengine.StrategyUsageEngineRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.secretflow.secretpad.service.model.strategyusageengine.StrategyUsageExecutionResult;

@Slf4j
@Service
@RequiredArgsConstructor
public class StrategyUsageEngineServiceImpl implements org.secretflow.secretpad.service.StrategyUsageEngineService {
    private final StrategyUsageRepository usageRepository;
    private final StrategyUsageInfoRepository infoRepository;
    // 核心修改：通过此 Service 调用 StrategyUsageDynamicRepository
    private final StrategyUsageDynamicServiceImpl strategyUsageDynamicService;
    private final StrategyUsageDynamicRepository strategyUsageDynamicRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void validate(StrategyUsageEngineRequest request) {
        log.info("Start strategy check: Project={}",
                request.getProjectId());

        LocalDateTime operationTime = LocalDateTime.now();
        String currentPhase = "FETCH_STRATEGY";
        StrategyUsageContext context = null;

        try {
            // 1. 获取策略
            context = fetchStrategyContext(request);

            // 2. 环境与主体检查
            currentPhase = "ENVIRONMENT_AND_SUBJECT";
            checkEnvironmentAndSubject(context.getInfo(), request);

//             4. 行为权限检查 (对交付和操作)
             currentPhase = "ACTION_PERMISSION";
             checkActionPermission(context.getInfo(), request);

            // 记录成功日志
            strategyUsageDynamicService.record(
                    context.getUsage(),
                    context.getInfo(),
                    request,
                    operationTime,
                    "PASS",
                    "FINISHED",
                    "Strategy check passed",
                    null
            );

            log.info("Strategy check passed (Phase 1).");
        } catch (SecretpadException e) {
            // 记录拦截日志
            strategyUsageDynamicService.record(
                    context == null ? null : context.getUsage(),
                    context == null ? null : context.getInfo(),
                    request,
                    operationTime,
                    "REJECT",
                    currentPhase,
                    buildErrorMessage(e),
                    e.getErrorCode() == null ? null : String.valueOf(e.getErrorCode().getCode())
            );
            throw e;
        } catch (Exception e) {
            // 记录异常日志
            strategyUsageDynamicService.record(
                    context == null ? null : context.getUsage(),
                    context == null ? null : context.getInfo(),
                    request,
                    operationTime,
                    "ERROR",
                    currentPhase,
                    e.getMessage(),
                    "UNEXPECTED_ERROR"
            );
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StrategyUsageExecutionResult validateAndInject(StrategyUsageEngineRequest request) {
        // 先走一遍既有的强校验流程
        validate(request);

        // 获取该策略并准备参数注入
        StrategyUsageContext context = fetchStrategyContext(request);
        Map<String, Object> injectedParams = new LinkedHashMap<>();

        // TODO: 在这里解析义务(compliance_obligations)，组装 injectedParams
        // 例如：
        // injectedParams.put("setEncrypt", true);
        
        return StrategyUsageExecutionResult.builder()
                .isPassed(true)
                .injectedParams(injectedParams)
                .build();
    }

    private StrategyUsageContext fetchStrategyContext(StrategyUsageEngineRequest request) {
        // 根据项目 ID 获取策略内容
        StrategyUsageDO usageDO = usageRepository.findByProjectId(request.getProjectId())
                .orElseThrow(() -> SecretpadException.of(
                        StrategyUsageControlErrorCode.STRATEGY_NOT_FOUND,
                        String.format("Project %s is not found", request.getProjectId())
                ));

        StrategyUsageInfoDO infoDO = infoRepository.findById(usageDO.getInfoId())
                .orElseThrow(() -> SecretpadException.of(
                        StrategyUsageControlErrorCode.STRATEGY_NOT_FOUND,
                        "Strategy Info ID: " + usageDO.getInfoId()
                ));
        return new StrategyUsageContext(usageDO, infoDO);
    }

    private void checkEnvironmentAndSubject(StrategyUsageInfoDO s, StrategyUsageEngineRequest r) {
        LocalDateTime now = LocalDateTime.now();
        // 定义格式化器
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        //如果策略中未设置生效时间和过期时间，则默认策略永不过期且立即生效，无需校验
        // 前端必须要传地址等信息，否则无法进行后续校验
        // 1. 生效时间校验
        if (s.getValidStartTime() != null && now.isBefore(s.getValidStartTime())) {
            throwAuthError(String.format("current time [%s] is before strategy effective time [%s]",
                    now.format(dateTimeFormatter), s.getValidStartTime().format(dateTimeFormatter)));
        }

        // 2. 过期时间校验
        if (s.getValidEndTime() != null && now.isAfter(s.getValidEndTime())) {
            throwAuthError(String.format("strategy has expired at [%s]. current time: [%s]",
                    s.getValidEndTime().format(dateTimeFormatter), now.format(dateTimeFormatter)));
        }

        // 3. 每日时间窗口校验 (HH:mm:ss)
        if (StringUtils.isNoneBlank(s.getDailyStartTime(), s.getDailyEndTime())) {
            LocalTime nowTime = now.toLocalTime();
            LocalTime start = LocalTime.parse(s.getDailyStartTime());
            LocalTime end = LocalTime.parse(s.getDailyEndTime());
            if (nowTime.isBefore(start) || nowTime.isAfter(end)) {
                throwAuthError(String.format("current time [%s] is not within allowed daily window [%s - %s]",
                        nowTime.format(timeFormatter), s.getDailyStartTime(), s.getDailyEndTime()));
            }
        }

        // 4. 星期几校验
        if (StringUtils.isNotBlank(s.getTimeWindowDays())) {
            int currentDay = now.getDayOfWeek().getValue();
            if (!checkCsvContains(s.getTimeWindowDays(), String.valueOf(currentDay))) {
                throwAuthError(String.format("current week day [%d] is not allowed. permitted days: [%s]",
                        currentDay, s.getTimeWindowDays()));
            }
        }

        // (位置、地域、运行环境等由于前端难以提供，暂不在此处强制校验)

        // 8. 用户校验
        if (StringUtils.isNotBlank(s.getAllowedUsers())
                && !checkCsvContains(s.getAllowedUsers(), r.getUserId())) {
            throwAuthError(String.format("current user [%s] is not allowed. permitted users: [%s]",
                    r.getUserId(), s.getAllowedUsers()));
        }

        // 9. 角色校验
        if (StringUtils.isNotBlank(s.getRoleLimit())
                && !checkCsvContains(s.getRoleLimit(), r.getUserRole())) {
            throwAuthError(String.format("current role [%s] is not allowed. permitted roles: [%s]",
                    r.getUserRole(), s.getRoleLimit()));
        }

        // 10. 连接器校验
        if (StringUtils.isNotBlank(s.getUsageConnectors())
                && !checkCsvContains(s.getUsageConnectors(), r.getConnectorId())) {
            throwAuthError(String.format("current connector [%s] is not allowed. permitted connectors: [%s]",
                    r.getConnectorId(), s.getUsageConnectors()));
        }
    }

    private void checkActionPermission(StrategyUsageInfoDO s, StrategyUsageEngineRequest r) {
        // deliveryType 是分号分隔的多值字段，如 "1;2;3" 或 "1;2"
        // 当前仅校验不出域交付 (type=2)
        if (StringUtils.isBlank(s.getDeliveryType())) {
            throwAuthError("delivery type is not configured");
        }

        // 检查 deliveryType 中是否包含 "2"（不出域交付）
        boolean hasInternal = false;
        for (String type : s.getDeliveryType().split(";")) {
            if ("2".equals(type.trim())) {
                hasInternal = true;
                break;
            }
        }

        if (!hasInternal) {
            throwAuthError("strategy does not include internal delivery (type=2)");
        }

        String jsonConfig = s.getInternalDelivery();
        if (StringUtils.isBlank(jsonConfig)) {
            throwAuthError("internal delivery is declared but has no config");
        }

        try {
            JsonNode node = objectMapper.readTree(jsonConfig);

            // A. 是否启用
            if (node.has("enabled") && !node.get("enabled").asBoolean()) {
                throwAuthError("internal delivery is disabled by strategy");
            }

            // B. 单次上限校验 (maxSingleLimit)

            // C. 累计次数校验 (maxTotalCount) & 频率限制校验
            checkRateAndLimitFromNode(node, r.getProjectId());

            // D. 累计额度校验 (maxTotalLimit)

        } catch (SecretpadException se) {
            throw se;
        } catch (Exception e) {
            log.error("Parse internal_delivery JSON error", e);
            throwAuthError("strategy internal delivery config is invalid");
        }
    }

    /**
     * 校验累计使用次数和频率窗口限制
     */
    private void checkRateAndLimitFromNode(JsonNode node, String projectId) {
        if (node.has("maxTotalCount")) {
            long maxTotal = node.get("maxTotalCount").asLong();
            long actualTotal = strategyUsageDynamicRepository.countByProjectIdAndCheckResult(projectId, "PASS");
            if (actualTotal >= maxTotal) {
                throwAuthError(String.format("total usage count [%d] reaches strategy limit [%d]",
                        actualTotal, maxTotal));
            }
        }

        if (node.has("rateLimit")) {
            JsonNode rate = node.get("rateLimit");
            int rateCount = rate.get("count").asInt();
            String timeUnit = rate.get("timeUnit").asText();

            LocalDateTime windowStart = calculateWindowStart(timeUnit);
            long recentCount = strategyUsageDynamicRepository.countByProjectIdAndCheckResultAndCurrentOperationTimeAfter(
                    projectId, "PASS", windowStart);

            if (recentCount >= rateCount) {
                throwAuthError(String.format("rate limit exceeded: [%d] requests in last [%s], limit is [%d]",
                        recentCount, timeUnit, rateCount));
            }
        }
    }

    // 辅助方法：计算频率窗口起始时间，从当前时间开始，倒退一个单位
    private LocalDateTime calculateWindowStart(String unit) {
        if (StringUtils.isBlank(unit)) {
            // 如果单位为空，默认按分钟处理，或者抛出异常
            return LocalDateTime.now().minusMinutes(1);
        }

        LocalDateTime now = LocalDateTime.now();

        // 使用 switch 处理不同单位 (忽略大小写)
        switch (unit.toUpperCase()) {
            case "SECONDS":
                return now.minusSeconds(1);
            case "MINUTES":
                return now.minusMinutes(1);
            case "HOURS":
                return now.minusHours(1);
            case "DAYS":
                return now.minusDays(1);
            default:
                // 未知单位默认回退到一分钟，并记录警告日志
                log.warn("Unknown rateLimit timeUnit: {}, fallback to MINUTES", unit);
                return now.minusMinutes(1);
        }
    }


    private void throwAuthError(String detail) {
        throw SecretpadException.of(StrategyUsageControlErrorCode.STRATEGY_CHECK_FAILED, detail);
    }

    private boolean checkCsvContains(String csvList, String target) {
        if (csvList == null || target == null) {
            return false;
        }
        String[] items = csvList.split(",");
        for (String item : items) {
            if (item.trim().equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }

    private String buildErrorMessage(SecretpadException e) {
        if (StringUtils.isNotBlank(e.getMessage())) {
            return e.getMessage();
        }
        if (e.getArgs() != null && e.getArgs().length > 0) {
            return e.getArgs()[0];
        }
        return e.getErrorCode() == null ? "strategy usage validation failed" : e.getErrorCode().getMessageKey();
    }

    @Getter
    @RequiredArgsConstructor
    private static class StrategyUsageContext {
        private final StrategyUsageDO usage;
        private final StrategyUsageInfoDO info;
    }
}
