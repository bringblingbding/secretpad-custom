package org.secretflow.secretpad.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "strategy_usage")
@Getter
@Setter

// 注意：提供的 SQL 中没有 is_deleted 字段。如果后续添加了该字段，请取消注释以下内容并继承 BaseAggregationRoot
@SQLDelete(sql = "UPDATE strategy_usage SET is_deleted = 1 WHERE strategy_id = ?")
@Where(clause = "is_deleted = 0")
public class StrategyUsageDO extends BaseAggregationRoot<StrategyUsageDO>{
    // id, gmt_create, gmt_modified, is_deleted 通常在父类 BaseAggregationRoot 或通过 JPA Auditing 处理
    /**
     * 策略id，作为主键
     */
    @Id
    @Column(name = "strategy_id",nullable = false,length = 255)
    private String strategyId;

    /**
     * 数据表id
     */
    @Column(name = "table_id", length = 64)
    private String tableId;

    /**
     * 授权的项目id
     */
    @Column(name = "project_id", length = 64)
    private String projectId;

    /**
     * 使用的策略模板id
     */
    @Column(name = "strategy_template_id", length = 255)
    private String strategyTemplateId;

    /**
     * 对应的策略内容id，外键
     * 注意：这里对应 strategy_usage_info 表的主键
     */
    @Column(name = "info_id",nullable = false,length = 255)
    private String infoId;

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