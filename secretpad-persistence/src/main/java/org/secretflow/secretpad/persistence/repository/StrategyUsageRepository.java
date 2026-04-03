package org.secretflow.secretpad.persistence.repository;

import org.secretflow.secretpad.persistence.entity.StrategyUsageDO;
import org.secretflow.secretpad.persistence.entity.StrategyUsageInfoDO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StrategyUsageRepository extends BaseRepository<StrategyUsageDO, String> {


    /* ================== 新增 ================== */
    @Modifying
    @Transactional
    @Query(value =
            "INSERT INTO strategy_usage ( " +
                    "  strategy_id, table_id, project_id, strategy_template_id, info_id, " +
                    "  gmt_create, gmt_modified, is_deleted " +
                    ") VALUES ( " +
                    "  :#{#dto.strategyId}, :#{#dto.tableId}, :#{#dto.projectId}, :#{#dto.strategyTemplateId}, :#{#dto.infoId}, " +
                    "  :#{#dto.gmtCreate}, :#{#dto.gmtModified}, 0 " +
                    ")",
            nativeQuery = true)
    int insert(@Param("dto") StrategyUsageDO dto);



    //动态更新方法
    /* ================== 根据strategy_id修改（空值忽略），不更新info_id ================== */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value =
            "UPDATE strategy_usage SET " +
                    "  table_id             = CASE WHEN :#{#dto.tableId}            IS NULL THEN table_id             ELSE :#{#dto.tableId}            END, " +
                    "  project_id           = CASE WHEN :#{#dto.projectId}          IS NULL THEN project_id           ELSE :#{#dto.projectId}          END, " +
                    "  strategy_template_id = CASE WHEN :#{#dto.strategyTemplateId} IS NULL THEN strategy_template_id ELSE :#{#dto.strategyTemplateId} END, " +
                    "  gmt_modified         = CASE WHEN :#{#dto.gmtModified}        IS NULL THEN gmt_modified         ELSE :#{#dto.gmtModified}        END " +
                    "WHERE strategy_id = :#{#dto.strategyId} AND is_deleted = 0",
            nativeQuery = true)
    int update(@Param("dto") StrategyUsageDO dto);

    /* ================== 动态条件查询 ================== */
    @Query(value =
            "SELECT * FROM strategy_usage " +
                    "WHERE (:#{#dto.strategyId}         IS NULL OR strategy_id          = :#{#dto.strategyId}) " +
                    "  AND (:#{#dto.tableId}            IS NULL OR table_id             = :#{#dto.tableId}) " +
                    "  AND (:#{#dto.projectId}          IS NULL OR project_id           = :#{#dto.projectId}) " +
                    "  AND (:#{#dto.strategyTemplateId} IS NULL OR strategy_template_id = :#{#dto.strategyTemplateId}) " +
                    "  AND (:#{#dto.infoId}             IS NULL OR info_id              = :#{#dto.infoId}) " +
                    "  AND is_deleted = 0",
            nativeQuery = true)
    List<StrategyUsageDO> select(@Param("dto") StrategyUsageDO dto);

    /* ================== 删除 ================== */
    // 物理删除（按主键 strategy_id）
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM strategy_usage WHERE strategy_id = :strategyId", nativeQuery = true)
    int hardDelete(@Param("strategyId") Integer strategyId);

    /* ================== 逻辑删除 ================== */
    @Modifying
    @Transactional
    @Query(value = "UPDATE strategy_usage SET is_deleted = 1, gmt_modified = NOW() WHERE strategy_id = :strategyId", nativeQuery = true)
    int softDelete(@Param("strategyId") String strategyId);

    /**
     * 根据项目ID和数据表ID查找策略关联关系
     */
    Optional<StrategyUsageDO> findByProjectId(String projectId);
}