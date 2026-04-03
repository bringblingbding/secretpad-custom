package org.secretflow.secretpad.service.impl;

import lombok.RequiredArgsConstructor;
import org.secretflow.secretpad.common.util.PageUtils;
import org.secretflow.secretpad.common.util.UUIDUtils;
import org.secretflow.secretpad.persistence.entity.StrategyUsageInfoDO;
import org.secretflow.secretpad.persistence.entity.StrategyUsageTemplateDO;
import org.secretflow.secretpad.persistence.repository.StrategyUsageInfoRepository;
import org.secretflow.secretpad.persistence.repository.StrategyUsageTemplateRepository;
import org.secretflow.secretpad.service.StrategyUsageTemplateService;
import org.secretflow.secretpad.service.model.platformstrategytemplate.AllPlatformStrategyTemplateListVO;
import org.secretflow.secretpad.service.model.strategyusagetemplate.StrategyUsageTemplatePageRequestVO;
import org.secretflow.secretpad.service.model.strategyusagetemplate.AllStrategyUsageTemplateListVO;
import org.secretflow.secretpad.service.model.strategyusagetemplate.StrategyUsageTemplateWithInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StrategyUsageTemplateServiceImpl implements StrategyUsageTemplateService{

    private final StrategyUsageTemplateRepository strategyUsageTemplateRepository;
    private final StrategyUsageInfoRepository strategyUsageInfoRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createStrategyTemplate(StrategyUsageTemplateWithInfoVO request) {

        // 1. 手动生成 UUID
        String templateId = UUIDUtils.random(8);
        String infoId = UUIDUtils.random(8);
        LocalDateTime now = LocalDateTime.now();

        // 1. 先保存 Info 表 (详情)
        StrategyUsageInfoDO infoDO = new StrategyUsageInfoDO();
        BeanUtils.copyProperties(request, infoDO); // 复制时间、地区等属性
        infoDO.setInfoId(infoId);
        infoDO.setGmtCreate(now);
        infoDO.setGmtModified(now);

        strategyUsageInfoRepository.insert(infoDO);


        // 2. 再保存 StrategyUsageTemplate 表 (主表)
        StrategyUsageTemplateDO templateDO = StrategyUsageTemplateDO.builder()
                .strategyTemplateId(templateId)
                .strategyTemplateName(request.getStrategyTemplateName())
                .strategyTemplateScene(request.getStrategyTemplateScene())
                .strategyTemplateDesc(request.getStrategyTemplateDesc())
                .strategyTemplateType(request.getStrategyTemplateType())
                .infoId(infoId) // 关联外键
                .isActive(request.getIsActive())
                .build();
        templateDO.setGmtCreate(now);
        templateDO.setGmtModified(now);


        strategyUsageTemplateRepository.insert(templateDO);
        // 返回策略id strategyTemplateId 是UUID
        return templateId;
    }

    @Override
    public StrategyUsageTemplateWithInfoVO getStrategyTemplate(String strategyTemplateId) {
        // 1. 查主表
        StrategyUsageTemplateDO templateDO = strategyUsageTemplateRepository.findById(strategyTemplateId)
                .orElseThrow(() -> new RuntimeException("策略模板不存在: " + strategyTemplateId));

        // 2. 查详情表
        StrategyUsageInfoDO infoDO = strategyUsageInfoRepository.findById(templateDO.getInfoId())
                    .orElse(new StrategyUsageInfoDO());

        // 3. 组装两个表返回对象 (使用之前写的 from 方法)
        return StrategyUsageTemplateWithInfoVO.from(templateDO, infoDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStrategyTemplate(StrategyUsageTemplateWithInfoVO request) {
        String templateId = request.getStrategyTemplateId();
        if (templateId == null) {
            throw new RuntimeException("策略模板ID不能为空");
        }

        // 1. 查出旧主表记录，获取 infoId
        StrategyUsageTemplateDO oldTemplateDO = strategyUsageTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("策略模板不存在: " + templateId));

        LocalDateTime now = LocalDateTime.now();
        // 2. 更新 Info 表 (如果有必要且 infoId 存在)
        if (oldTemplateDO.getInfoId() != null) {
            StrategyUsageInfoDO infoUpdateDO = new StrategyUsageInfoDO();
            BeanUtils.copyProperties(request, infoUpdateDO);
            // 关键：设置 infoId，否则 repository 不知道更新哪条
            infoUpdateDO.setInfoId(oldTemplateDO.getInfoId());
            infoUpdateDO.setGmtModified(now);
            strategyUsageInfoRepository.update(infoUpdateDO);
        }

        // 3. 更新 StrategyUsageTemplate 表
        StrategyUsageTemplateDO templateUpdateDO = StrategyUsageTemplateDO.builder()
                .strategyTemplateId(templateId)
                .strategyTemplateName(request.getStrategyTemplateName())
                .strategyTemplateScene(request.getStrategyTemplateScene())
                .strategyTemplateDesc(request.getStrategyTemplateDesc())
                .strategyTemplateType(request.getStrategyTemplateType())
                .isActive(request.getIsActive())
                .build();

        templateUpdateDO.setGmtModified(now);
        // 调用主表的自定义 Update (动态SQL)
        strategyUsageTemplateRepository.update(templateUpdateDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStrategyTemplate(String strategyTemplateId) {
        // 1. 查主表获取 infoId

        StrategyUsageTemplateDO templateDO = strategyUsageTemplateRepository.findById(strategyTemplateId)
                .orElseThrow(() -> new RuntimeException("策略模板不存在: " + strategyTemplateId));


        // 2. 逻辑删除主表
        strategyUsageTemplateRepository.softDelete(templateDO.getStrategyTemplateId());

        // 3. 物理删除详情表
        String infoId = templateDO.getInfoId();
        if (infoId != null) {
            strategyUsageInfoRepository.softDelete(infoId);
        }

    }

    //写一个/selectByPage方法，返回分页结果
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AllStrategyUsageTemplateListVO selectByPage(StrategyUsageTemplatePageRequestVO request){
        // 1. 获取查询条件对象，策略模板，可以传null
        StrategyUsageTemplateDO queryDO = request.getStrategyUsageTemplateDO();


        // 2. 调用私有方法获取全量聚合数据
        List<StrategyUsageTemplateDO> allList = strategyUsageTemplateRepository.select(queryDO);

        // 3. 处理分页参
        int pageNum = request.getPageNum() != null ? request.getPageNum() : 1;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 10;

        // 4. 执行内存分页 (PageUtils)
        List<StrategyUsageTemplateDO> pagedList = PageUtils.rangeList(allList, pageSize, pageNum);

        // 5. 封装返回对象，pagedList是DO
        AllStrategyUsageTemplateListVO allStrategyUsageTemplateListVO = new AllStrategyUsageTemplateListVO();
        allStrategyUsageTemplateListVO.setStrategyUsageTemplateVOList(pagedList);
        allStrategyUsageTemplateListVO.setStrategyUsageTemplateNums(allList.size());

        return allStrategyUsageTemplateListVO;
     }

    // list all without page
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AllStrategyUsageTemplateListVO list(StrategyUsageTemplateDO request) {
        // 1. 获取查询条件对象，策略模板，可以传null
        StrategyUsageTemplateDO queryDO = request;

        // 2. 调用私有方法获取全量聚合数据
        List<StrategyUsageTemplateDO> allList = strategyUsageTemplateRepository.select(queryDO);

        // 3. 封装返回对象
        AllStrategyUsageTemplateListVO allStrategyUsageTemplateListVO = new AllStrategyUsageTemplateListVO();
        allStrategyUsageTemplateListVO.setStrategyUsageTemplateVOList(allList);
        allStrategyUsageTemplateListVO.setStrategyUsageTemplateNums(allList.size());

        return allStrategyUsageTemplateListVO;
    }

}