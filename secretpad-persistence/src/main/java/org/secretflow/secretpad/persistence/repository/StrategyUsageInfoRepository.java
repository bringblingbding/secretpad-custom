package org.secretflow.secretpad.persistence.repository;

import org.secretflow.secretpad.persistence.entity.StrategyUsageInfoDO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StrategyUsageInfoRepository extends BaseRepository<StrategyUsageInfoDO, String> {
// <实体类型，主键类型>
    /* ================== 新增 ================== */
    // 注意：view 和 copy 是 MySQL 关键字，必须加反引号
    @Modifying
    @Transactional
    @Query(value =
            "INSERT INTO strategy_usage_info ( " +
                    "  info_id, valid_start_time, valid_end_time, time_window_days, daily_start_time, daily_end_time, " +
                    "  region_limit, position_limit, env_requirements, allowed_users, " +
                    "  delivery_connectors, usage_connectors, role_limit, required_data_state, " +
                    "  is_auto_destroy, is_vpn_required, allowed_protocols, encryption_channel, " +
                    "  is_storage_encrypted, storage_enc_algo, storage_locations, retention_days, " +
                    "  `view`, `copy`, download, " +
                    "  delivery_type, direct_delivery, internal_delivery, external_delivery, " +
                    "  application_controls, flow_control, compliance_obligations, " +
                    "  data_preparation, data_filtering, feature_processing, statistics, " +
                    "  model_training, model_prediction, model_evaluation, " +
                    "  gmt_create, gmt_modified, is_deleted " +
                    ") VALUES ( " +
                    "  :#{#dto.infoId}, :#{#dto.validStartTime}, :#{#dto.validEndTime}, :#{#dto.timeWindowDays}, :#{#dto.dailyStartTime}, :#{#dto.dailyEndTime}, " +
                    "  :#{#dto.regionLimit}, :#{#dto.positionLimit}, :#{#dto.envRequirements}, :#{#dto.allowedUsers}, " +
                    "  :#{#dto.deliveryConnectors}, :#{#dto.usageConnectors}, :#{#dto.roleLimit}, :#{#dto.requiredDataState}, " +
                    "  :#{#dto.isAutoDestroy}, :#{#dto.isVpnRequired}, :#{#dto.allowedProtocols}, :#{#dto.encryptionChannel}, " +
                    "  :#{#dto.isStorageEncrypted}, :#{#dto.storageEncAlgo}, :#{#dto.storageLocations}, :#{#dto.retentionDays}, " +
                    "  :#{#dto.view}, :#{#dto.copy}, :#{#dto.download}, " +
                    "  :#{#dto.deliveryType}, :#{#dto.directDelivery}, :#{#dto.internalDelivery}, :#{#dto.externalDelivery}, " +
                    "  :#{#dto.applicationControls}, :#{#dto.flowControl}, :#{#dto.complianceObligations}, " +
                    "  :#{#dto.dataPreparation}, :#{#dto.dataFiltering}, :#{#dto.featureProcessing}, :#{#dto.statistics}, " +
                    "  :#{#dto.modelTraining}, :#{#dto.modelPrediction}, :#{#dto.modelEvaluation}, " +
                    "  :#{#dto.gmtCreate}, :#{#dto.gmtModified}, 0 " +
                    ")",
            nativeQuery = true)
    int insert(@Param("dto") StrategyUsageInfoDO dto);


    /* ================== 根据info_id修改（空值忽略） ================== */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value =
            "UPDATE strategy_usage_info SET " +
                    // --- 时间限制 ---
                    "  valid_start_time   = CASE WHEN :#{#dto.validStartTime} IS NULL THEN valid_start_time ELSE :#{#dto.validStartTime} END, " +
                    "  valid_end_time     = CASE WHEN :#{#dto.validEndTime}   IS NULL THEN valid_end_time   ELSE :#{#dto.validEndTime}   END, " +
                    "  time_window_days   = CASE WHEN :#{#dto.timeWindowDays} IS NULL THEN time_window_days ELSE :#{#dto.timeWindowDays} END, " +
                    "  daily_start_time   = CASE WHEN :#{#dto.dailyStartTime} IS NULL THEN daily_start_time ELSE :#{#dto.dailyStartTime} END, " +
                    "  daily_end_time     = CASE WHEN :#{#dto.dailyEndTime}   IS NULL THEN daily_end_time   ELSE :#{#dto.dailyEndTime}   END, " +
                    // --- 地域与环境 ---
                    "  region_limit       = CASE WHEN :#{#dto.regionLimit}     IS NULL THEN region_limit     ELSE :#{#dto.regionLimit}     END, " +
                    "  position_limit     = CASE WHEN :#{#dto.positionLimit}   IS NULL THEN position_limit   ELSE :#{#dto.positionLimit}   END, " +
                    "  env_requirements   = CASE WHEN :#{#dto.envRequirements} IS NULL THEN env_requirements ELSE :#{#dto.envRequirements} END, " +
                    // --- 主体与角色 ---
                    "  allowed_users      = CASE WHEN :#{#dto.allowedUsers}    IS NULL THEN allowed_users    ELSE :#{#dto.allowedUsers}    END, " +
                    "  role_limit         = CASE WHEN :#{#dto.roleLimit}       IS NULL THEN role_limit       ELSE :#{#dto.roleLimit}       END, " +
                    // --- 连接器 ---
                    "  delivery_connectors = CASE WHEN :#{#dto.deliveryConnectors} IS NULL THEN delivery_connectors ELSE :#{#dto.deliveryConnectors} END, " +
                    "  usage_connectors    = CASE WHEN :#{#dto.usageConnectors}    IS NULL THEN usage_connectors    ELSE :#{#dto.usageConnectors}    END, " +
                    // --- 数据状态与销毁 ---
                    "  required_data_state = CASE WHEN :#{#dto.requiredDataState} IS NULL THEN required_data_state ELSE :#{#dto.requiredDataState} END, " +
                    "  is_auto_destroy     = CASE WHEN :#{#dto.isAutoDestroy}     IS NULL THEN is_auto_destroy     ELSE :#{#dto.isAutoDestroy}     END, " +
                    // --- 通信与加密 ---
                    "  is_vpn_required      = CASE WHEN :#{#dto.isVpnRequired}      IS NULL THEN is_vpn_required      ELSE :#{#dto.isVpnRequired}      END, " +
                    "  allowed_protocols    = CASE WHEN :#{#dto.allowedProtocols}   IS NULL THEN allowed_protocols    ELSE :#{#dto.allowedProtocols}   END, " +
                    "  encryption_channel   = CASE WHEN :#{#dto.encryptionChannel}  IS NULL THEN encryption_channel   ELSE :#{#dto.encryptionChannel}  END, " +
                    "  is_storage_encrypted = CASE WHEN :#{#dto.isStorageEncrypted} IS NULL THEN is_storage_encrypted ELSE :#{#dto.isStorageEncrypted} END, " +
                    "  storage_enc_algo     = CASE WHEN :#{#dto.storageEncAlgo}     IS NULL THEN storage_enc_algo     ELSE :#{#dto.storageEncAlgo}     END, " +
                    "  storage_locations    = CASE WHEN :#{#dto.storageLocations}   IS NULL THEN storage_locations    ELSE :#{#dto.storageLocations}   END, " +
                    "  retention_days       = CASE WHEN :#{#dto.retentionDays}      IS NULL THEN retention_days       ELSE :#{#dto.retentionDays}      END, " +
                    // --- 操作行为 (注意 view/copy 关键字) ---
                    "  `view`             = CASE WHEN :#{#dto.view}           IS NULL THEN `view`             ELSE :#{#dto.view}           END, " +
                    "  `copy`             = CASE WHEN :#{#dto.copy}           IS NULL THEN `copy`             ELSE :#{#dto.copy}           END, " +
                    "  download           = CASE WHEN :#{#dto.download}       IS NULL THEN download           ELSE :#{#dto.download}       END, " +
                    "  delivery_type      = CASE WHEN :#{#dto.deliveryType}   IS NULL THEN delivery_type      ELSE :#{#dto.deliveryType}   END, " +
                    "  direct_delivery    = CASE WHEN :#{#dto.directDelivery} IS NULL THEN direct_delivery    ELSE :#{#dto.directDelivery} END, " +
                    "  internal_delivery  = CASE WHEN :#{#dto.internalDelivery} IS NULL THEN internal_delivery ELSE :#{#dto.internalDelivery} END, " +
                    "  external_delivery  = CASE WHEN :#{#dto.externalDelivery} IS NULL THEN external_delivery ELSE :#{#dto.externalDelivery} END, " +
                    "  application_controls = CASE WHEN :#{#dto.applicationControls} IS NULL THEN application_controls ELSE :#{#dto.applicationControls} END, " +
                    "  flow_control       = CASE WHEN :#{#dto.flowControl}    IS NULL THEN flow_control       ELSE :#{#dto.flowControl}    END, " +
                    // --- 用途 ---
                    "  compliance_obligations = CASE WHEN :#{#dto.complianceObligations} IS NULL THEN compliance_obligations ELSE :#{#dto.complianceObligations} END, " +
                    "  data_preparation       = CASE WHEN :#{#dto.dataPreparation}       IS NULL THEN data_preparation       ELSE :#{#dto.dataPreparation}       END, " +
                    "  data_filtering         = CASE WHEN :#{#dto.dataFiltering}         IS NULL THEN data_filtering         ELSE :#{#dto.dataFiltering}         END, " +
                    "  feature_processing     = CASE WHEN :#{#dto.featureProcessing}     IS NULL THEN feature_processing     ELSE :#{#dto.featureProcessing}     END, " +
                    "  statistics             = CASE WHEN :#{#dto.statistics}            IS NULL THEN statistics             ELSE :#{#dto.statistics}            END, " +
                    "  model_training         = CASE WHEN :#{#dto.modelTraining}         IS NULL THEN model_training         ELSE :#{#dto.modelTraining}         END, " +
                    "  model_prediction       = CASE WHEN :#{#dto.modelPrediction}       IS NULL THEN model_prediction       ELSE :#{#dto.modelPrediction}       END, " +
                    "  model_evaluation       = CASE WHEN :#{#dto.modelEvaluation}       IS NULL THEN model_evaluation       ELSE :#{#dto.modelEvaluation}       END, " +
                    "  gmt_modified           = CASE WHEN :#{#dto.gmtModified}           IS NULL THEN gmt_modified           ELSE :#{#dto.gmtModified}           END " +
            "WHERE info_id = :#{#dto.infoId} AND is_deleted = 0",
            nativeQuery = true)
    int update(@Param("dto") StrategyUsageInfoDO dto);

    /* ================== 根据任意项条件查询，动态条件查询（空值忽略） ================== */
    @Query(value =
            "SELECT * FROM strategy_usage_info " +
                    "WHERE (:#{#dto.infoId} IS NULL OR info_id = :#{#dto.infoId}) " +
                    // 简单匹配
                    "  AND (:#{#dto.validStartTime} IS NULL OR valid_start_time = :#{#dto.validStartTime}) " +
                    "  AND (:#{#dto.validEndTime} IS NULL OR valid_end_time = :#{#dto.validEndTime}) " +
                    "  AND (:#{#dto.regionLimit} IS NULL OR region_limit = :#{#dto.regionLimit}) " +
                    "  AND (:#{#dto.allowedUsers} IS NULL OR allowed_users = :#{#dto.allowedUsers}) " +
                    // 此处省略了部分字段的查询条件，如果需要全字段查询请参考上方 update 模式补全
                    // 通常查询不需要对所有 JSON 大字段(如 view, flow_control) 进行 where 匹配
                    "  AND (:#{#dto.deliveryType} IS NULL OR delivery_type = :#{#dto.deliveryType}) " +
                    "  AND (:#{#dto.isAutoDestroy} IS NULL OR is_auto_destroy = :#{#dto.isAutoDestroy}) " +
                    "  AND is_deleted = 0",
            nativeQuery = true)
    List<StrategyUsageInfoDO> select(@Param("dto") StrategyUsageInfoDO dto);

    /* ================== 删除 ================== */
    // 物理删除（按主键 info_id）
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM strategy_usage_info WHERE info_id = :infoId", nativeQuery = true)
    int hardDelete(@Param("infoId") Integer infoId);
    /* ================== 逻辑删除 ================== */
    @Modifying
    @Transactional
    @Query(value = "UPDATE strategy_usage_info SET is_deleted = 1, gmt_modified = NOW() WHERE info_id = :infoId", nativeQuery = true)
    int softDelete(@Param("infoId") String infoId);
}