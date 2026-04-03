package org.secretflow.secretpad.service.model.platformstrategytemplate;

import lombok.*;
import org.secretflow.secretpad.persistence.entity.PlatformStrategyTemplateDO;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllPlatformStrategyTemplateListVO {
    List<PlatformStrategyTemplateDO> platformStrategyTemplateVOList;

    Integer platformStrategyTemplateNums;
}
