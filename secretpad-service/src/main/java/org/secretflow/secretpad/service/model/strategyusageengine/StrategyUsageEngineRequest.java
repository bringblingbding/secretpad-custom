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

}
