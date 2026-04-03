package org.secretflow.secretpad.service.impl;

import org.checkerframework.checker.units.qual.A;
import org.secretflow.secretpad.persistence.entity.PlatformStrategyTemplateDO;
import org.secretflow.secretpad.persistence.repository.PlatformStrategyTemplateRepository;
import org.secretflow.secretpad.service.PlatformStrategyTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformStrategyTemplateServiceImpl implements PlatformStrategyTemplateService {
    @Autowired
    private PlatformStrategyTemplateRepository platformStrategyTemplateRepository;

    @Override
    public void insert(PlatformStrategyTemplateDO request) {
        platformStrategyTemplateRepository.insert(request);
    }

    @Override
    public void update(PlatformStrategyTemplateDO request) {
        platformStrategyTemplateRepository.update(request);
    }

    @Override
    public void delete(PlatformStrategyTemplateDO request) {
        platformStrategyTemplateRepository.delete(request);
    }

    @Override
    public List<PlatformStrategyTemplateDO> select(PlatformStrategyTemplateDO request) {
        return platformStrategyTemplateRepository.select(request);
    }
}
