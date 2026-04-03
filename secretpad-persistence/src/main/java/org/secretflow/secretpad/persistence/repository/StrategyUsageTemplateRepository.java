package org.secretflow.secretpad.persistence.repository;

import org.secretflow.secretpad.persistence.entity.StrategyUsageTemplateDO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StrategyUsageTemplateRepository extends BaseRepository<StrategyUsageTemplateDO, String> {

    /* ================== 新增, strategy_template_id手动生成,is_deleted 字段被硬编码为 0================== */
    @Modifying
    @Transactional
    @Query(value =
            "INSERT INTO strategy_usage_template ( " +
                    "  strategy_template_id, strategy_template_name, strategy_template_scene, strategy_template_desc, " +
                    "  strategy_template_type, info_id, is_active, " +
                    "  gmt_create, gmt_modified, is_deleted " +
                    ") VALUES ( " +
                    "  :#{#dto.strategyTemplateId}, :#{#dto.strategyTemplateName}, :#{#dto.strategyTemplateScene}, :#{#dto.strategyTemplateDesc}, " +
                    "  :#{#dto.strategyTemplateType}, :#{#dto.infoId}, :#{#dto.isActive}, " +
                    "  :#{#dto.gmtCreate}, :#{#dto.gmtModified}, 0 " +
                    ")",
            nativeQuery = true)
    int insert(@Param("dto") StrategyUsageTemplateDO dto);

    /* ================== 修改（空值忽略），不更新info_id ================== */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value =
            "UPDATE strategy_usage_template SET " +
                    "  strategy_template_name  = CASE WHEN :#{#dto.strategyTemplateName}  IS NULL THEN strategy_template_name  ELSE :#{#dto.strategyTemplateName}  END, " +
                    "  strategy_template_scene = CASE WHEN :#{#dto.strategyTemplateScene} IS NULL THEN strategy_template_scene ELSE :#{#dto.strategyTemplateScene} END, " +
                    "  strategy_template_desc  = CASE WHEN :#{#dto.strategyTemplateDesc}  IS NULL THEN strategy_template_desc  ELSE :#{#dto.strategyTemplateDesc}  END, " +
                    "  strategy_template_type  = CASE WHEN :#{#dto.strategyTemplateType}  IS NULL THEN strategy_template_type  ELSE :#{#dto.strategyTemplateType}  END, " +
                    "  is_active               = CASE WHEN :#{#dto.isActive}              IS NULL THEN is_active               ELSE :#{#dto.isActive}              END, " +
                    "  gmt_modified            = CASE WHEN :#{#dto.gmtModified}           IS NULL THEN gmt_modified            ELSE :#{#dto.gmtModified}           END " +
                    "WHERE strategy_template_id = :#{#dto.strategyTemplateId} AND is_deleted = 0",
            nativeQuery = true)
    int update(@Param("dto") StrategyUsageTemplateDO dto);

    /* ================== 动态条件查询（空值忽略） ================== */
    @Query(value =
            "SELECT * FROM strategy_usage_template " +
                    "WHERE (:#{#dto.strategyTemplateId}   IS NULL OR strategy_template_id  = :#{#dto.strategyTemplateId}) " +
                    "  AND (:#{#dto.strategyTemplateName} IS NULL OR strategy_template_name LIKE CONCAT('%',:#{#dto.strategyTemplateName},'%')) " +
                    "  AND (:#{#dto.strategyTemplateScene} IS NULL OR strategy_template_scene = :#{#dto.strategyTemplateScene}) " +
                    "  AND (:#{#dto.strategyTemplateDesc} IS NULL OR strategy_template_desc LIKE CONCAT('%',:#{#dto.strategyTemplateDesc},'%')) " +
                    "  AND (:#{#dto.strategyTemplateType} IS NULL OR strategy_template_type = :#{#dto.strategyTemplateType}) " +
                    "  AND (:#{#dto.infoId}               IS NULL OR info_id               = :#{#dto.infoId}) " +
                    "  AND (:#{#dto.isActive}             IS NULL OR is_active             = :#{#dto.isActive}) " +
                    "  AND is_deleted = 0",
            nativeQuery = true)
    List<StrategyUsageTemplateDO> select(@Param("dto") StrategyUsageTemplateDO dto);

    /* ================== 删除 ================== */

    // 物理删除（按业务键）
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM strategy_usage_template WHERE strategy_template_id = :strategyTemplateId", nativeQuery = true)
    int hardDelete(@Param("strategyTemplateId") String strategyTemplateId);

    // 逻辑删除（按业务键，补充方法）
    @Modifying
    @Transactional
    @Query(value = "UPDATE strategy_usage_template SET is_deleted = 1, gmt_modified = NOW() WHERE strategy_template_id = :strategyTemplateId", nativeQuery = true)
    int softDelete(@Param("strategyTemplateId") String strategyTemplateId);
}