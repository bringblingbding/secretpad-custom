package org.secretflow.secretpad.service.model.strategyusage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.secretflow.secretpad.persistence.entity.StrategyUsageDO;
import org.secretflow.secretpad.persistence.entity.StrategyUsageInfoDO;
import org.secretflow.secretpad.service.model.strategyusageinfo.StrategyUsageInfoVO;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class StrategyUsageWithInfoVO extends StrategyUsageInfoVO {

    @Schema(description = "策略关联ID")
    private String strategyId;

    @Schema(description = "数据表ID")
    private String tableId;

    @Schema(description = "项目ID")
    private String projectId;

    @Schema(description = "模板ID")
    private String strategyTemplateId;

    // ================= 新增同步所需字段 =================
    @Schema(description = "发起方节点ID (用于 Kuscia 同步)")
    private String srcNodeId;

    @Schema(description = "接收方节点ID (用于 Kuscia 同步)")
    private String dstNodeId;
    // ===================================================

    /**
     * 将双表 DO 转换为聚合 VO
     *
     * @param usageDO 关联表 DO
     * @param infoDO  详情表 DO
     * @return 聚合 VO
     */
    public static StrategyUsageWithInfoVO from(StrategyUsageDO usageDO, StrategyUsageInfoDO infoDO) {
        if (usageDO == null) {
            return null;
        }

        StrategyUsageWithInfoVO vo = new StrategyUsageWithInfoVO();

        // 1. 设置 Usage 表特有的关联字段
        vo.setStrategyId(usageDO.getStrategyId());
        vo.setTableId(usageDO.getTableId());
        vo.setProjectId(usageDO.getProjectId());
        vo.setStrategyTemplateId(usageDO.getStrategyTemplateId());
        // 设置同步节点 ID（如果查询结果中包含这些信息）
        vo.setSrcNodeId(usageDO.getSrcNodeId());
        vo.setDstNodeId(usageDO.getDstNodeId());

        // 2. 设置 Info 表的详情字段
        if (infoDO != null) {
            // 利用 StrategyUsageInfoVO 已经写好的转换逻辑，生成一个临时 VO
            StrategyUsageInfoVO infoVO = StrategyUsageInfoVO.from(infoDO);

            // 将临时 VO 的属性复制给当前的 detailVO (因为它们是继承关系)
            BeanUtils.copyProperties(infoVO, vo);
        }

        return vo;
    }
}