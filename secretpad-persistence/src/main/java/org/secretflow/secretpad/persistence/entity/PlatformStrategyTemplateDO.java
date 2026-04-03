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
@Table(name = "platform_strategy_template")
@ToString
@Getter
@Setter
@SQLDelete(sql = "UPDATE platform_strategy_template SET is_deleted = 1 WHERE strategy_template_id = ?")
@Where(clause = "is_deleted = 0")
public class PlatformStrategyTemplateDO extends BaseAggregationRoot<PlatformStrategyTemplateDO>{
    /**
     * 业务主键 / 策略模板id（varchar(255)）
     */
    @Id
    @Column(name = "strategy_template_id", length = 255, nullable = false)
    private String strategyTemplateId;

    @Column(name = "strategy_template_name", length = 255)
    private String strategyTemplateName;

    @Column(name = "strategy_template_scene", length = 255)
    private String strategyTemplateScene;

    @Column(name = "strategy_template_desc", length = 255)
    private String strategyTemplateDesc;

    @Column(name = "strategy_id")
    private Integer strategyId;

    @Transient
    private Integer pageNum;

    @Transient
    private Integer pageSize;

}
