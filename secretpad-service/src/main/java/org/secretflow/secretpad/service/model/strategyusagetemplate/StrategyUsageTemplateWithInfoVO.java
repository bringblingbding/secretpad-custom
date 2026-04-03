package org.secretflow.secretpad.service.model.strategyusagetemplate;

import lombok.*;
import org.secretflow.secretpad.persistence.entity.StrategyUsageInfoDO;
import org.secretflow.secretpad.persistence.entity.StrategyUsageTemplateDO;
import org.secretflow.secretpad.service.model.strategyusageinfo.StrategyUsageInfoVO;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyUsageTemplateWithInfoVO extends StrategyUsageInfoVO {

    //相比StrategyUsageTemplateDO少了id、nodeId、projectId等字段
    //继承自StrategyUsageInfoVO，包含info表的字段

    /**
     * 策略模板ID (业务主键)UUID
     */
    private String strategyTemplateId;

    /**
     * 策略模板名称
     */
    private String strategyTemplateName;

    /**
     * 适用场景
     */
    private String strategyTemplateScene;

    /**
     * 描述
     */
    private String strategyTemplateDesc;

    /**
     * 模板类型：0-内置，1-自定义
     */
    private String strategyTemplateType;


    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;
    /**
     * 是否删除
     */
    private Boolean isDeleted;

    /**
     * 状态
     */
    private String isActive;


    /**
     * 将双表 DO 转换为聚合 VO 返回给前端
     *
     * @param usageTemplateDO 关联表 DO
     * @param infoDO  详情表 DO
     * @return 聚合 VO
     */
    public static StrategyUsageTemplateWithInfoVO from(StrategyUsageTemplateDO usageTemplateDO, StrategyUsageInfoDO infoDO) {
        if (usageTemplateDO == null) {
            return null;
        }

        StrategyUsageTemplateWithInfoVO vo = new StrategyUsageTemplateWithInfoVO();

        // 1. 设置 StrategyUsageTemplate 表特有的关联字段
        vo.setStrategyTemplateId(usageTemplateDO.getStrategyTemplateId());
        vo.setStrategyTemplateName(usageTemplateDO.getStrategyTemplateName());
        vo.setStrategyTemplateScene(usageTemplateDO.getStrategyTemplateScene());
        vo.setStrategyTemplateDesc(usageTemplateDO.getStrategyTemplateDesc());
        vo.setStrategyTemplateType(usageTemplateDO.getStrategyTemplateType());
        vo.setGmtCreate(usageTemplateDO.getGmtCreate());
        vo.setGmtModified(usageTemplateDO.getGmtModified());
        vo.setIsActive(usageTemplateDO.getIsActive());
        vo.setIsDeleted(usageTemplateDO.getIsDeleted());



        // 2. 设置 Info 表的详情字段
        if (infoDO != null) {
            // 利用 StrategyUsageInfoVO 已经写好的转换逻辑，生成一个临时 VO
            StrategyUsageInfoVO infoVO = StrategyUsageInfoVO.from(infoDO);

            // 将临时 VO 的属性复制给当前的vo
            BeanUtils.copyProperties(infoVO, vo);
        }

        return vo;
    }

}