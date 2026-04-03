package org.secretflow.secretpad.persistence.repository;

import org.secretflow.secretpad.persistence.entity.PlatformStrategyTemplateDO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
public interface PlatformStrategyTemplateRepository extends BaseRepository<PlatformStrategyTemplateDO, String> {

    /* ================== 新增 ================== */
    @Modifying
    @Transactional
    @Query(value =
            "INSERT INTO platform_strategy_template ( " +
                    "  strategy_template_id, strategy_template_name, strategy_template_scene, strategy_template_desc, " +
                    "  strategy_id, gmt_create, gmt_modified, is_deleted " +
                    ") VALUES ( " +
                    "  :#{#dto.strategyTemplateId}, :#{#dto.strategyTemplateName}, :#{#dto.strategyTemplateScene}, :#{#dto.strategyTemplateDesc}, " +
                    "  :#{#dto.strategyId}, :#{#dto.gmtCreate}, :#{#dto.gmtModified}, 0 " +
                    ")",
            nativeQuery = true)
    int insert(@Param("dto") PlatformStrategyTemplateDO dto);

    /* ================== 修改（空值忽略） ================== */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value =
            "UPDATE platform_strategy_template SET " +
                    "  strategy_template_name  = CASE WHEN :#{#dto.strategyTemplateName}  IS NULL THEN strategy_template_name  ELSE :#{#dto.strategyTemplateName}  END, " +
                    "  strategy_template_scene = CASE WHEN :#{#dto.strategyTemplateScene} IS NULL THEN strategy_template_scene ELSE :#{#dto.strategyTemplateScene} END, " +
                    "  strategy_template_desc  = CASE WHEN :#{#dto.strategyTemplateDesc}  IS NULL THEN strategy_template_desc  ELSE :#{#dto.strategyTemplateDesc}  END, " +
                    "  strategy_id             = CASE WHEN :#{#dto.strategyId}            IS NULL THEN strategy_id             ELSE :#{#dto.strategyId}            END, " +
                    "  gmt_modified            = CASE WHEN :#{#dto.gmtModified}           IS NULL THEN gmt_modified            ELSE :#{#dto.gmtModified}           END " +
                    "WHERE strategy_template_id = :#{#dto.strategyTemplateId} AND is_deleted = 0",
            nativeQuery = true)
    int update(@Param("dto") PlatformStrategyTemplateDO dto);

    /* ================== 动态条件查询（空值忽略） ================== */
    @Query(value =
            "SELECT * FROM platform_strategy_template " +
                    "WHERE (:#{#dto.strategyTemplateId}   IS NULL OR strategy_template_id  = :#{#dto.strategyTemplateId}) " +
                    "  AND (:#{#dto.strategyTemplateName} IS NULL OR strategy_template_name LIKE CONCAT('%',:#{#dto.strategyTemplateName},'%')) " +
                    "  AND (:#{#dto.strategyTemplateScene} IS NULL OR strategy_template_scene = :#{#dto.strategyTemplateScene}) " +
                    "  AND (:#{#dto.strategyTemplateDesc} IS NULL OR strategy_template_desc LIKE CONCAT('%',:#{#dto.strategyTemplateDesc},'%')) " +
                    "  AND (:#{#dto.strategyId}           IS NULL OR strategy_id           = :#{#dto.strategyId}) " +
                    "  AND is_deleted = 0",
            nativeQuery = true)
    List<PlatformStrategyTemplateDO> select(@Param("dto") PlatformStrategyTemplateDO dto);

    /* ================== 删除 ================== */

    // 物理删除（按业务键）
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM platform_strategy_template WHERE strategy_template_id = :strategyTemplateId", nativeQuery = true)
    int hardDelete(@Param("strategyTemplateId") String strategyTemplateId);


}
