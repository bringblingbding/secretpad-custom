package org.secretflow.secretpad.service.impl;

import lombok.RequiredArgsConstructor;
import org.secretflow.secretpad.common.util.UUIDUtils;
import org.secretflow.secretpad.persistence.entity.StrategyUsageDO;
import org.secretflow.secretpad.persistence.entity.StrategyUsageInfoDO;
import org.secretflow.secretpad.persistence.repository.StrategyUsageInfoRepository;
import org.secretflow.secretpad.persistence.repository.StrategyUsageRepository;
import org.secretflow.secretpad.service.StrategyUsageService;
import org.secretflow.secretpad.service.model.strategyusage.StrategyUsageWithInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.util.HashSet;
import java.util.Set;
@Service
@RequiredArgsConstructor
public class StrategyUsageServiceImpl implements StrategyUsageService {

    private final StrategyUsageRepository strategyUsageRepository;
    private final StrategyUsageInfoRepository strategyUsageInfoRepository;

    /**
     * 获取对象中所有值为 null 的属性名
     */
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createStrategy(StrategyUsageWithInfoVO request) {
        // 1. 手动生成 UUID
        String strategyId = UUIDUtils.random(8);
        String infoId = UUIDUtils.random(8);
        LocalDateTime now = LocalDateTime.now();

        // 2. 先保存 Info 表 (详情)
        StrategyUsageInfoDO infoDO = new StrategyUsageInfoDO();
        BeanUtils.copyProperties(request, infoDO); // 复制时间、地区等属性
        infoDO.setInfoId(infoId); // 手动设置
        // 父类字段通过 Setter 设置
        infoDO.setGmtCreate(now);
        infoDO.setGmtModified(now);

        // 设置同步节点信息并调用 save
        infoDO.setSrcNodeId(request.getSrcNodeId());
        infoDO.setDstNodeId(request.getDstNodeId());
        strategyUsageInfoRepository.save(infoDO); // 修改点：使用 save 触发同步

        // 2. 再保存 Usage 表 (关联)
        StrategyUsageDO usageDO = StrategyUsageDO.builder()
                .strategyId(strategyId) // 手动设置
                .tableId(request.getTableId())
                .projectId(request.getProjectId())
                .strategyTemplateId(request.getStrategyTemplateId())
                .infoId(infoId)
                .build();
        usageDO.setGmtCreate(now);
        usageDO.setGmtModified(now);
        // 设置同步节点信息并调用 save
        usageDO.setSrcNodeId(request.getSrcNodeId());
        usageDO.setDstNodeId(request.getDstNodeId());

        strategyUsageRepository.save(usageDO);

        return strategyId;
    }

    @Override
    public StrategyUsageWithInfoVO getStrategy(String strategyId) {
        // 1. 查关联表 (使用 JPA findById 即可，前提是 Entity 上有 @Where(clause="is_deleted=0"))
        StrategyUsageDO usageDO = strategyUsageRepository.findById(strategyId)
                .orElseThrow(() -> new RuntimeException("策略不存在: " + strategyId));

        // 2. 查详情表
        StrategyUsageInfoDO infoDO = strategyUsageInfoRepository.findById(usageDO.getInfoId())
                .orElse(new StrategyUsageInfoDO());

        // 3. 组装两个表返回对象
        return StrategyUsageWithInfoVO.from(usageDO, infoDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStrategy(StrategyUsageWithInfoVO request) {
        if (request.getStrategyId() == null) {
            throw new RuntimeException("策略不存在无法更新");
        }

        // 1. 获取数据库中的旧记录（JPA 更新推荐先查后改，以保证同步数据的完整性）
        StrategyUsageDO usageDO = strategyUsageRepository.findById(request.getStrategyId())
                .orElseThrow(() -> new RuntimeException("策略不存在"));

        StrategyUsageInfoDO infoDO = strategyUsageInfoRepository.findById(usageDO.getInfoId())
                .orElseThrow(() -> new RuntimeException("策略详情不存在"));

        LocalDateTime now = LocalDateTime.now();
        // 获取请求中所有的 null 字段名，以便在复制时忽略它们
        String[] ignoreProperties = getNullPropertyNames(request);
        // 2. 更新 Info 表(忽略空值)
        // 将 request 中的新值复制到查出来的 infoDO 中
        BeanUtils.copyProperties(request, infoDO,ignoreProperties);
        infoDO.setInfoId(usageDO.getInfoId()); // 保持 ID 不变
        infoDO.setGmtModified(now);

        // 设置同步节点信息并 save
        strategyUsageInfoRepository.save(infoDO); // 修改点：使用 save 触发同步

        // 3. 更新 Usage 表(忽略空值)
        BeanUtils.copyProperties(request, usageDO, ignoreProperties);
        usageDO.setStrategyId(request.getStrategyId()); // 强制保持 ID
        usageDO.setGmtModified(now);

        // 设置同步节点信息并 save
        strategyUsageRepository.save(usageDO); // 修改点：使用 save 触发同步
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStrategy(String strategyId) {
        // 1. 查关联
        StrategyUsageDO usageDO = strategyUsageRepository.findById(strategyId)
                .orElseThrow(() -> new RuntimeException("策略不存在"));

        String infoId = usageDO.getInfoId();

        // 2. 逻辑删除 Usage (调用 Repository 的 softDelete)
        strategyUsageRepository.softDelete(strategyId);

        // 3. 逻辑删除 Info
        if (infoId != null) {
            strategyUsageInfoRepository.softDelete(infoId);
        }
    }
}