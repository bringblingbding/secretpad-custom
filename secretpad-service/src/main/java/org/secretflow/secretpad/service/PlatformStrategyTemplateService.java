package org.secretflow.secretpad.service;

//import org.secretflow.secretpad.persistence.entity.PlatformConnectorDO;
import org.secretflow.secretpad.persistence.entity.PlatformStrategyTemplateDO;

import java.util.List;

public interface PlatformStrategyTemplateService {
    void insert(PlatformStrategyTemplateDO request);

    List<PlatformStrategyTemplateDO> select(PlatformStrategyTemplateDO request);

    void update(PlatformStrategyTemplateDO request);

    void delete(PlatformStrategyTemplateDO request);
}
