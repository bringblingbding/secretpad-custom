package org.secretflow.secretpad.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.secretflow.secretpad.common.errorcode.StrategyUsageControlErrorCode;
import org.secretflow.secretpad.common.exception.SecretpadException;
import org.secretflow.secretpad.persistence.entity.StrategyUsageDO;
import org.secretflow.secretpad.persistence.entity.StrategyUsageInfoDO;
import org.secretflow.secretpad.persistence.repository.StrategyUsageInfoRepository;
import org.secretflow.secretpad.persistence.repository.StrategyUsageRepository;
import org.secretflow.secretpad.service.model.strategyusageengine.StrategyUsageEngineRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class StrategyUsageEngineServiceImpl implements org.secretflow.secretpad.service.StrategyUsageEngineService {
    private final StrategyUsageRepository usageRepository;
    private final StrategyUsageInfoRepository infoRepository;
    // 核心修改：通过此 Service 调用 StrategyUsageDynamicRepository
    private final StrategyUsageDynamicServiceImpl strategyUsageDynamicService;

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

//            // 3. 数据状态与通信检查
//            currentPhase = "DATA_STATE_AND_COMMUNICATION";
//            checkDataStateAndCommunication(context.getInfo(), request);

            // 4. 行为权限检查 (按需取消注释)
            // currentPhase = "ACTION_PERMISSION";
            // checkActionPermission(context.getInfo(), request);

            // currentPhase = "STORAGE_CONSTRAINT";
            // checkStorageConstraint(context.getInfo(), request);

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

        // 5. 位置限制 (Position Limit)
        if (StringUtils.isNotBlank(s.getPositionLimit())
                && !checkCsvContains(s.getPositionLimit(), r.getCurrentPosition())) {
            throwAuthError(String.format("current position [%s] is not allowed. permitted: [%s]",
                    r.getCurrentPosition(), s.getPositionLimit()));
        }

        // 6. 地域限制 (Region Limit)
        if (StringUtils.isNotBlank(s.getRegionLimit())
                && !checkCsvContains(s.getRegionLimit(), r.getCurrentRegion())) {
            throwAuthError(String.format("current region [%s] is not allowed. permitted: [%s]",
                    r.getCurrentRegion(), s.getRegionLimit()));
        }

        // 7. 环境要求 (Environment Requirements)
        if (StringUtils.isNotBlank(s.getEnvRequirements())
                && !checkCsvContains(s.getEnvRequirements(), r.getEnvironmentType())) {
            throwAuthError(String.format("current environment [%s] is not allowed. requirements: [%s]",
                    r.getEnvironmentType(), s.getEnvRequirements()));
        }

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

    private void checkDataStateAndCommunication(StrategyUsageInfoDO s, StrategyUsageEngineRequest r) {
        if (StringUtils.isNotBlank(s.getRequiredDataState()) && StringUtils.isNotBlank(r.getCurrentDataState())) {
            if (!s.getRequiredDataState().equalsIgnoreCase(r.getCurrentDataState())) {
                throwAuthError("current data state does not match strategy requirement: " + s.getRequiredDataState());
            }
        }

        if (Boolean.TRUE.equals(s.getIsVpnRequired()) && !Boolean.TRUE.equals(r.getIsVpnUsed())) {
            throwAuthError("vpn is required");
        }
    }

    private void checkActionPermission(StrategyUsageInfoDO s, StrategyUsageEngineRequest r) {

    }


    private void checkStorageConstraint(StrategyUsageInfoDO s, StrategyUsageEngineRequest r) {

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
