package org.secretflow.secretpad.persistence.repository;

import org.secretflow.secretpad.persistence.entity.StrategyUsageDynamicDO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 策略动态使用记录 Repository
 */
@Repository
public interface StrategyUsageDynamicRepository extends BaseRepository<StrategyUsageDynamicDO, String> {

    long countByProjectId(String projectId);

    /* ================== 新增 ================== */
    @Modifying
    @Transactional
    @Query(value =
            "INSERT INTO strategy_usage_dynamic ( " +
                    "  usage_id, strategy_id, table_id, project_id, usage_count, current_operation_time, " +
                    "  region, position, env, users, usage_connectors, role, " +
                    "  required_data_state, protocols, storage_locations, retention_days, " +
                    "  check_result, terminate_phase, terminate_reason, error_code, " +
                    "  gmt_create, gmt_modified, is_deleted " +
                    ") VALUES ( " +
                    "  :#{#dto.usageId}, :#{#dto.strategyId}, :#{#dto.tableId}, :#{#dto.projectId}, :#{#dto.usageCount}, :#{#dto.currentOperationTime}, " +
                    "  :#{#dto.region}, :#{#dto.position}, :#{#dto.env}, :#{#dto.users}, :#{#dto.usageConnectors}, :#{#dto.role}, " +
                    "  :#{#dto.requiredDataState}, :#{#dto.protocols}, :#{#dto.storageLocations}, :#{#dto.retentionDays}, " +
                    "  :#{#dto.checkResult}, :#{#dto.terminatePhase}, :#{#dto.terminateReason}, :#{#dto.errorCode}, " +
                    "  :#{#dto.gmtCreate}, :#{#dto.gmtModified}, 0 " +
                    ")",
            nativeQuery = true)
    int insert(@Param("dto") StrategyUsageDynamicDO dto);


    /* ================== 根据 usage_id 修改（空值忽略） ================== */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value =
            "UPDATE strategy_usage_dynamic SET " +
                    "  strategy_id            = CASE WHEN :#{#dto.strategyId} IS NULL THEN strategy_id ELSE :#{#dto.strategyId} END, " +
                    "  table_id               = CASE WHEN :#{#dto.tableId} IS NULL THEN table_id ELSE :#{#dto.tableId} END, " +
                    "  project_id             = CASE WHEN :#{#dto.projectId} IS NULL THEN project_id ELSE :#{#dto.projectId} END, " +
                    "  usage_count            = CASE WHEN :#{#dto.usageCount} IS NULL THEN usage_count ELSE :#{#dto.usageCount} END, " +
                    "  current_operation_time = CASE WHEN :#{#dto.currentOperationTime} IS NULL THEN current_operation_time ELSE :#{#dto.currentOperationTime} END, " +
                    "  region                 = CASE WHEN :#{#dto.region} IS NULL THEN region ELSE :#{#dto.region} END, " +
                    "  position               = CASE WHEN :#{#dto.position} IS NULL THEN position ELSE :#{#dto.position} END, " +
                    "  env                    = CASE WHEN :#{#dto.env} IS NULL THEN env ELSE :#{#dto.env} END, " +
                    "  users                  = CASE WHEN :#{#dto.users} IS NULL THEN users ELSE :#{#dto.users} END, " +
                    "  usage_connectors       = CASE WHEN :#{#dto.usageConnectors} IS NULL THEN usage_connectors ELSE :#{#dto.usageConnectors} END, " +
                    "  role                   = CASE WHEN :#{#dto.role} IS NULL THEN role ELSE :#{#dto.role} END, " +
                    "  required_data_state    = CASE WHEN :#{#dto.requiredDataState} IS NULL THEN required_data_state ELSE :#{#dto.requiredDataState} END, " +
                    "  protocols              = CASE WHEN :#{#dto.protocols} IS NULL THEN protocols ELSE :#{#dto.protocols} END, " +
                    "  storage_locations      = CASE WHEN :#{#dto.storageLocations} IS NULL THEN storage_locations ELSE :#{#dto.storageLocations} END, " +
                    "  retention_days         = CASE WHEN :#{#dto.retentionDays} IS NULL THEN retention_days ELSE :#{#dto.retentionDays} END, " +
                    "  check_result           = CASE WHEN :#{#dto.checkResult} IS NULL THEN check_result ELSE :#{#dto.checkResult} END, " +
                    "  terminate_phase        = CASE WHEN :#{#dto.terminatePhase} IS NULL THEN terminate_phase ELSE :#{#dto.terminatePhase} END, " +
                    "  terminate_reason       = CASE WHEN :#{#dto.terminateReason} IS NULL THEN terminate_reason ELSE :#{#dto.terminateReason} END, " +
                    "  error_code             = CASE WHEN :#{#dto.errorCode} IS NULL THEN error_code ELSE :#{#dto.errorCode} END, " +
                    "  gmt_modified           = CASE WHEN :#{#dto.gmtModified} IS NULL THEN gmt_modified ELSE :#{#dto.gmtModified} END " +
                    "WHERE usage_id = :#{#dto.usageId} AND is_deleted = 0",
            nativeQuery = true)
    int update(@Param("dto") StrategyUsageDynamicDO dto);


    /* ================== 动态条件查询（空值忽略） ================== */
    @Query(value =
            "SELECT * FROM strategy_usage_dynamic " +
                    "WHERE (:#{#dto.usageId} IS NULL OR usage_id = :#{#dto.usageId}) " +
                    "  AND (:#{#dto.strategyId} IS NULL OR strategy_id = :#{#dto.strategyId}) " +
                    "  AND (:#{#dto.projectId} IS NULL OR project_id = :#{#dto.projectId}) " +
                    "  AND (:#{#dto.tableId} IS NULL OR table_id = :#{#dto.tableId}) " +
                    "  AND (:#{#dto.checkResult} IS NULL OR check_result = :#{#dto.checkResult}) " +
                    "  AND (:#{#dto.errorCode} IS NULL OR error_code = :#{#dto.errorCode}) " +
                    "  AND is_deleted = 0",
            nativeQuery = true)
    List<StrategyUsageDynamicDO> select(@Param("dto") StrategyUsageDynamicDO dto);


    /* ================== 物理删除 ================== */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM strategy_usage_dynamic WHERE usage_id = :usageId", nativeQuery = true)
    int hardDelete(@Param("usageId") String usageId);


    /* ================== 逻辑删除 ================== */
    @Modifying
    @Transactional
    @Query(value = "UPDATE strategy_usage_dynamic SET is_deleted = 1, gmt_modified = NOW() WHERE usage_id = :usageId", nativeQuery = true)
    int softDelete(@Param("usageId") String usageId);

    // 统计该项目下某种检查结果的累计次数（用于 maxTotalCount）
    long countByProjectIdAndCheckResult(String projectId, String checkResult);

    // 统计该项目在特定时间范围内的成功次数（用于 rateLimit）
//    SELECT COUNT(*)
//    FROM strategy_usage_dynamic
//    WHERE project_id = ?              -- 1. 限定项目
//    AND check_result = 'PASS'       -- 2. 只计算成功的记录
//    AND current_operation_time > ?  -- 3. 只计算某个时间点之后的记录
    long countByProjectIdAndCheckResultAndCurrentOperationTimeAfter(String projectId, String checkResult, LocalDateTime time);
}