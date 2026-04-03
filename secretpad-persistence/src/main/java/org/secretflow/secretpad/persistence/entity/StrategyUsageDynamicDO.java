package org.secretflow.secretpad.persistence.entity;

import org.secretflow.secretpad.persistence.converter.SqliteLocalDateTimeConverter;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

/**
 * 策略动态使用记录实体类
 * 继承自 BaseAggregationRoot，自动获得 id, isDeleted, gmtCreate, gmtModified 字段
 */
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "strategy_usage_dynamic")
@ToString(callSuper = true) // 包含父类字段
@Getter
@Setter
// 这里的 SQLDelete 指向的是父类定义的 is_deleted 字段
@SQLDelete(sql = "UPDATE strategy_usage_dynamic SET is_deleted = 1 WHERE usage_id = ?")
@Where(clause = "is_deleted = 0")
public class StrategyUsageDynamicDO extends BaseAggregationRoot<StrategyUsageDynamicDO> {

    /**
     * 业务主键 - 对应数据库 bigint(20)
     */
    @Id
    @Column(name = "usage_id",length = 255, nullable = false)
    private String usageId;

    /**
     * 策略ID
     */
    @Column(name = "strategy_id", length = 255)
    private String strategyId;

    /**
     * 表ID
     */
    @Column(name = "table_id", length = 255)
    private String tableId;

    /**
     * 项目ID
     * 注意：由于父类实现了 ProjectNodesInfo 并有一个 getProjectId() 方法，
     * 建议重写该方法或直接使用此字段。
     */
    @Column(name = "project_id", length = 255)
    private String projectId;

    /**
     * 使用次数
     */
    @Column(name = "usage_count")
    private Integer usageCount;

    /**
     * 当前操作时间
     * 使用基类中同样的 SqliteLocalDateTimeConverter
     */
    @Column(name = "current_operation_time")
    @Convert(converter = SqliteLocalDateTimeConverter.class)
    private LocalDateTime currentOperationTime;

    /**
     * 使用地域
     */
    @Column(name = "region", length = 255)
    private String region;

    /**
     * 位置标识
     */
    @Column(name = "position", length = 255)
    private String position;

    /**
     * 运行环境
     */
    @Column(name = "env", length = 255)
    private String env;

    /**
     * 使用用户/组织
     */
    @Column(name = "users", length = 255)
    private String users;

    /**
     * 使用连接器
     */
    @Column(name = "usage_connectors", length = 255)
    private String usageConnectors;

    /**
     * 角色限制
     */
    @Column(name = "role", length = 255)
    private String role;

    /**
     * 要求的数据状态
     */
    @Column(name = "required_data_state", length = 255)
    private String requiredDataState;

    /**
     * 传输协议
     */
    @Column(name = "protocols", length = 255)
    private String protocols;

    /**
     * 存储位置
     */
    @Column(name = "storage_locations", length = 255)
    private String storageLocations;

    /**
     * 存储天数
     */
    @Column(name = "retention_days")
    private Integer retentionDays;

    /**
     * 检查结果
     */
    @Column(name = "check_result", length = 255)
    private String checkResult;

    /**
     * 终止阶段
     */
    @Column(name = "terminate_phase", length = 255)
    private String terminatePhase;

    /**
     * 终止原因
     */
    @Column(name = "terminate_reason", columnDefinition = "TEXT")
    private String terminateReason;

    /**
     * 错误代码
     */
    @Column(name = "error_code", length = 255)
    private String errorCode;

    /**
     * 实现父类接口方法
     */
    @Override
    public String getProjectId() {
        return this.projectId;
    }
}