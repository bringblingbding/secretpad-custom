package org.secretflow.secretpad.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "strategy_usage_template")
@ToString
@Getter
@Setter
// 1. 核心注解：告诉 Hibernate，当有人调用 delete() 时，请偷偷替换成这条 UPDATE 语句
@SQLDelete(sql = "UPDATE strategy_usage_template SET is_deleted = 1 WHERE strategy_template_id = ?")
// 2. 核心注解：告诉 Hibernate，当有人查询数据时，自动带上 is_deleted = 0 这个条件
@Where(clause = "is_deleted = 0")
public class StrategyUsageTemplateDO extends BaseAggregationRoot<StrategyUsageTemplateDO> {

    // id, gmt_create, gmt_modified, is_deleted 通常在父类 BaseAggregationRoot 或通过 JPA Auditing 处理

    /**
     * 策略模板id (业务主键)
     */
    @Id
    @Column(name = "strategy_template_id", length = 255, nullable = false)
    private String strategyTemplateId;

    /**
     * 策略模板名称
     */
    @Column(name = "strategy_template_name", length = 255)
    private String strategyTemplateName;

    /**
     * 策略模板适用场景
     */
    @Column(name = "strategy_template_scene", length = 255)
    private String strategyTemplateScene;

    /**
     * 策略模板描述
     */
    @Column(name = "strategy_template_desc", length = 255)
    private String strategyTemplateDesc;

    /**
     * 模板类型，0表示内置模板，1表示自定义模板
     */
    @Column(name = "strategy_template_type", length = 255)
    private String strategyTemplateType;

    /**
     * 关联strategy_usage_info表info_id
     */
    @Column(name = "info_id",nullable = false,length = 255)
    private String infoId;

    /**
     * 状态
     */
    @Column(name = "is_active", length = 255)
    private String isActive;




}