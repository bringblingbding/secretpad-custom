package org.secretflow.secretpad.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "strategy_usage_info")
@ToString
@Getter
@Setter
// 注意：提供的 SQL 中没有 is_deleted 字段。如果后续添加了该字段，请取消注释以下内容并继承 BaseAggregationRoot
 @SQLDelete(sql = "UPDATE strategy_usage_info SET is_deleted = 1 WHERE info_id = ?")
 @Where(clause = "is_deleted = 0")
public class StrategyUsageInfoDO extends BaseAggregationRoot<StrategyUsageInfoDO> { //

    // id, gmt_create, gmt_modified, is_deleted 通常在父类 BaseAggregationRoot 或通过 JPA Auditing 处理

    /**
     * 策略内容的id，作为主键
     */
    @Id
    @Column(name = "info_id", length = 255, nullable = false)
    private String infoId;

    /**
     * 起始时间
     */
    @Column(name = "valid_start_time")
    private LocalDateTime validStartTime;

    /**
     * 结束时间
     */
    @Column(name = "valid_end_time")
    private LocalDateTime validEndTime;

    /**
     * 可使用的星期几
     */
    @Column(name = "time_window_days", length = 255)
    private String timeWindowDays;

    /**
     * 每天开始时间
     */
    @Column(name = "daily_start_time", length = 255)
    private String dailyStartTime;

    /**
     * 每天结束时间
     */
    @Column(name = "daily_end_time", length = 255)
    private String dailyEndTime;

    /**
     * 地点---限定使用地域（如行政区划码）
     */
    @Column(name = "region_limit", length = 255)
    private String regionLimit;

    /**
     * 限定使用位置标识（平台或连接器）
     */
    @Column(name = "position_limit", length = 255)
    private String positionLimit;

    /**
     * 限定运行环境（如沙箱、TEE）
     */
    @Column(name = "env_requirements", length = 255)
    private String envRequirements;

    /**
     * 主体---限定允许使用的用户或组织
     */
    @Column(name = "allowed_users", length = 255)
    private String allowedUsers;

    /**
     * 限定交付连接器
     */
    @Column(name = "delivery_connectors", length = 255)
    private String deliveryConnectors;

    /**
     * 限定使用连接器
     */
    @Column(name = "usage_connectors", length = 255)
    private String usageConnectors;

    /**
     * 限定角色（如数据服务方）
     */
    @Column(name = "role_limit", length = 255)
    private String roleLimit;

    /**
     * 客体---限定数据状态（如加密）
     */
    @Column(name = "required_data_state", length = 255)
    private String requiredDataState;

    /**
     * 使用完成后立即销毁原始数据
     */
    @Column(name = "is_auto_destroy")
    private Boolean isAutoDestroy;

    /**
     * 通信---是否强制使用 VPN
     */
    @Column(name = "is_vpn_required")
    private Boolean isVpnRequired;

    /**
     * 允许的传输协议
     */
    @Column(name = "allowed_protocols", length = 255)
    private String allowedProtocols;

    /**
     * 加密信道要求
     */
    @Column(name = "encryption_channel", length = 255)
    private String encryptionChannel;

    /**
     * 存储是否加密
     */
    @Column(name = "is_storage_encrypted")
    private Boolean isStorageEncrypted;

    /**
     * 存储加密算法
     */
    @Column(name = "storage_enc_algo", length = 255)
    private String storageEncAlgo;

    /**
     * 限定存储位置(如connector-001)
     */
    @Column(name = "storage_locations", length = 255)
    private String storageLocations;

    /**
     * 存储最长保留时间（单位：天）
     */
    @Column(name = "retention_days")
    private Integer retentionDays;

    /**
     * 查看 (JSON)
     * 注意：view 是 SQL 关键字，使用反引号或转义处理
     */
    @Column(name = "`view`", length = 255)
    private String view;

    /**
     * 复制 (JSON)
     */
    @Column(name = "`copy`", length = 255)
    private String copy;

    /**
     * 下载 (JSON)
     */
    @Column(name = "download", length = 255)
    private String download;

    /**
     * 交付方式，1代表直接交付，2代表不出域交付...
     */
    @Column(name = "delivery_type", length = 255)
    private String deliveryType;

    /**
     * 直接交付 (JSON)
     */
    @Column(name = "direct_delivery", length = 255)
    private String directDelivery;

    /**
     * 不出域交付 (JSON)
     */
    @Column(name = "internal_delivery", length = 255)
    private String internalDelivery;

    /**
     * 出域交付 (JSON)
     */
    @Column(name = "external_delivery", length = 255)
    private String externalDelivery;

    /**
     * 应用管控
     */
    @Column(name = "application_controls", length = 255)
    private String applicationControls;

    /**
     * 流转控制 (JSON)
     */
    @Column(name = "flow_control", length = 255)
    private String flowControl;

    /**
     * 履行义务
     */
    @Column(name = "compliance_obligations", length = 255)
    private String complianceObligations;

    /**
     * 数据准备
     */
    @Column(name = "data_preparation",columnDefinition = "TEXT")
    private String dataPreparation;

    /**
     * 数据过滤
     */
    @Column(name = "data_filtering", columnDefinition = "TEXT")
    private String dataFiltering;

    /**
     * 特征处理
     */
    @Column(name = "feature_processing",columnDefinition = "TEXT")
    private String featureProcessing;

    /**
     * 统计
     */
    @Column(name = "statistics",columnDefinition = "TEXT")
    private String statistics;

    /**
     * 模型训练
     */
    @Column(name = "model_training",columnDefinition = "TEXT")
    private String modelTraining;

    /**
     * 模型预测
     */
    @Column(name = "model_prediction",columnDefinition = "TEXT")
    private String modelPrediction;

    /**
     * 模型评估
     */
    @Column(name = "model_evaluation",columnDefinition = "TEXT")
    private String modelEvaluation;

    /**
     * Kuscia 同步专用：发起方节点 ID
     */
    @Column(name = "src_node_id", length = 255)
    private String srcNodeId;

    /**
     * Kuscia 同步专用：接收方节点 ID
     */
    @Column(name = "dst_node_id", length = 255)
    private String dstNodeId;



}