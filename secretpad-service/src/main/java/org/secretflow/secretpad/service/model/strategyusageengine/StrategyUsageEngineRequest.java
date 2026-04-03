package org.secretflow.secretpad.service.model.strategyusageengine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
// 引入 Swagger 注解
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 策略引擎请求上下文
 * 对应流程图右上角的 "输入请求" 及 Phase 1 所需的所有校验素材
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class StrategyUsageEngineRequest {
    // =======================================================
    // 1. 资源定位 (Resource Location)
    // =======================================================
    /**
     * 表id和项目id
     * 用于定位策略记录
     */
    @Schema(description = "项目ID", example = "test26")
    private String projectId;


    // =======================================================
    // 环境与位置 (Environment & Location) -> 对应 Phase 1
    // =======================================================


    /**
     * 当前所处的地理区域编码 (e.g., "CN-HZ")
     * 校验字段: region_limit
     */
    @Schema(description = "区域", example = "CN-BJ")
    private String currentRegion;

    /**
     * 当前计算/访问发生的平台标识 (Node ID, e.g.,platform-123)
     * 校验字段: position_limit
     */
    @Schema(description = "平台标识", example = "platform-123")
    private String currentPosition;
    /**
     * 当前运行环境类型 (e.g., "TEE", "Sandbox", "Local")
     * 校验字段: env_requirements
     */
    private String environmentType;


    // =======================================================
    // 主体信息 (Subject) -> 对应 Phase 1
    // =======================================================
    /**
     * 当前操作用户ID
     * 校验字段: allowed_users(e.g., yqtest)
     */
    @Schema(description = "用户ID", example = "yqtest")
    private String userId;

    /**
     * 当前用户所属角色
     * 校验字段: role_limit(e.g.dataProvider)
     */
    @Schema(description = "用户角色", example = "数据使用方，联合建模方")
    private String userRole;

    /**
     * 使用的连接器ID (Connector ID,point-one)
     * 校验字段: usage_connectors (计算时) 或 delivery_connectors (交付时)
     */
    @Schema(description = "连接器ID", example = "point-one")
    private String connectorId;

    // =======================================================
    // 客体 (Object ) -> 对应 Phase 1
    // =======================================================
    /**
     * 当前数据的状态 (e.g., "Plain", "Encrypted", "Masked")
     * 校验字段: required_data_state
     */
    private String currentDataState;

    // =======================================================
    // 通信 (Communication) -> 对应 Phase 1
    // =======================================================
    /**
     * 是否通过 VPN 访问
     * 校验字段: is_vpn_required
     */
    private Boolean isVpnUsed;

    /**
     * 当前通信使用的协议 (e.g., "HTTP", "GRPC", "PSI")
     * 校验字段: allowed_protocols
     */
    private String protocol;


    // =======================================================
    // 存储 (Storage) -> 对应 Phase 1
    // =======================================================

    /**
     * 结果产出的目标存储位置标识 (e.g., "oss-bucket-01")
     * 校验字段: storage_locations
     */
    private String targetStorageLocation;


    /**
     * 存储的保留天数
     * 校验字段: retention_days
     */
    private Integer retentionDays;

    // =======================================================
    // 动作与参数 (Action) -> 对应 Phase 1 & 2
    // =======================================================

    /**
     * 动作详细参数 (Map)
     * 用于校验 JSON 中的 "maxSingleLimit" 等约束
     *
     * 约定 Key 示例:
     * - "rows": 1000       (对应 unit: "ROWS")
     * - "dataSize": 2048   (对应 unit: "MB" 或 "KB")
     * - "algorithm": "XGB" (对应模型训练中的算法限制)
     */
    private Map<String, Object> actionParams;


}
