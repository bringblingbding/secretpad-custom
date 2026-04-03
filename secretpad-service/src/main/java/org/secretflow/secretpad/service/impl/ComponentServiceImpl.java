/*
 * Copyright 2023 Ant Group Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.secretflow.secretpad.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.secretflow.secretpad.common.errorcode.GraphErrorCode;
import org.secretflow.secretpad.common.exception.SecretpadException;
import org.secretflow.secretpad.common.util.FileUtils;
import org.secretflow.secretpad.common.util.JsonUtils;
import org.secretflow.secretpad.persistence.entity.StrategyUsageDO;
import org.secretflow.secretpad.persistence.entity.StrategyUsageInfoDO;
import org.secretflow.secretpad.persistence.repository.StrategyUsageInfoRepository;
import org.secretflow.secretpad.persistence.repository.StrategyUsageRepository;
import org.secretflow.secretpad.service.ComponentService;
import org.secretflow.secretpad.service.configuration.ScqlConfig;
import org.secretflow.secretpad.service.configuration.SecretFlowVersionConfig;
import org.secretflow.secretpad.service.configuration.SecretpadComponentConfig;
import org.secretflow.secretpad.service.constant.ComponentConstants;
import org.secretflow.secretpad.service.graph.ComponentTools;
import org.secretflow.secretpad.service.model.component.ComponentVersion;
import org.secretflow.secretpad.service.model.graph.*;
import com.secretflow.spec.v1.CompListDef;
import com.secretflow.spec.v1.ComponentDef;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.secretflow.proto.pipeline.Pipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.secretflow.secretpad.common.constant.DeployModeConstants.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Component service implementation class
 *
 * @author yansi
 * @date 2023/6/7
 */
@Slf4j
@Service
public class ComponentServiceImpl implements ComponentService {

    @Value("${component.i18n.location:./config/i18n}")
    private String i18nLocation;

    @Resource
    private List<CompListDef> components;

    @Resource
    private SecretpadComponentConfig secretpadComponentConfig;

    @Resource
    private SecretFlowVersionConfig secretFlowVersionConfig;

    @Resource
    private ScqlConfig scqlConfig;

    @Autowired
    private StrategyUsageRepository usageRepository; // 用于根据 project+table 找 strategy
    @Autowired
    private StrategyUsageInfoRepository infoRepository; // 用于找具体的 info 策略内容
    // 在类中定义转换器
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, CompListVO> listComponents(String projectId) {
//        1. 初始化与分类遍历（原始 components 列表）
        Map<String, CompListVO> resp = new HashMap<>();
        //获取并预解析策略：将 7 个 JSON 字段的内容全部合并到一个大 Map 中
        // 1. 如果 projectId 为空，直接解析出一个空的策略 Map (全放行)
        // 2. 如果 projectId 不为空，再去查库定位策略
        StrategyUsageInfoDO info = (projectId != null) ? getStrategyInfo(projectId) : null;
        Map<String, Boolean> strategyMap = parseAllStrategiesToFlatMap(info);

        components.forEach(compListDef -> {
            List<ComponentDef> comps = compListDef.getCompsList();
            if (!CollectionUtils.isEmpty(comps)) {
//                2.将原始的定义转换为 CompListVO，每一个 compListVO 代表一个库（如 secretflow 或 trustedflow）
                CompListVO compListVO = CompListVO.builder()
                        .name(compListDef.getName())
                        .version(compListDef.getVersion())
                        .desc(compListDef.getDesc())
                        .comps(new ArrayList<>()).build();
                compListVO.getComps().addAll(comps.stream()
                        .map(componentDef -> {
                            //secretflow/domain/name:version，secretflow/psi/psi:0.0.1
                            //黑名单校验：检查 secretpadComponentConfig（通常来自 application.yaml 配置文件）中的 hide 列表是否包含这个标识符。
                            // 如果包含，则将该组件添加到 SF_HIDE_COMPONENTS 中，并在最终返回的组件列表中排除它。
                            String hide = compListDef.getName() + "/" + componentDef.getDomain() + "/" + componentDef.getName() + ":" + componentDef.getVersion();
                            if (secretpadComponentConfig.getHide().contains(hide)) {
                                log.info("hide {}", hide);
                                SF_HIDE_COMPONENTS.put(componentDef.getName(), componentDef);
                                return null;//当前组件被隐藏，返回 null，在后续的 filter 过程中会被排除掉
                            }

                            // --- 新增逻辑：直接在大 Map 中根据算子名查找开关 ---
                            if (info != null) {
                                String name = componentDef.getName().toLowerCase();
                                // 如果策略 Map 中包含该算子名，且值为 false，则隐藏
                                // 如果策略为空，放行；如果策略不为空，但不包含该算子，放行；只有当策略不为空且包含该算子且值为 false 时才隐藏
                                if (strategyMap.containsKey(name) && Boolean.FALSE.equals(strategyMap.get(name))) {
                                    log.debug("hide by strategy: name={}", name);
                                    return null;
                                }
                            }

                            //3.将 ComponentDef（详细定义）简化映射为 ComponentSummaryDef（摘要定义）。
                            return ComponentSummaryDef.builder()
                                    .domain(componentDef.getDomain())
                                    .name(componentDef.getName())
                                    .version(componentDef.getVersion())
                                    .desc(componentDef.getDesc())
                                    .build();
                        })
                        .filter(Objects::nonNull).toList());//非常重要。这一步过滤掉了上一步中因为“黑名单”产生的 null 对象。
                resp.put(compListVO.getName(), compListVO);
            }
        });
        resp.remove(ComponentConstants.SECRETPAD);
        resp.remove(ComponentConstants.SCQL);
        return resp;
    }
    /**
     * 根据项目 ID 和表 ID 获取关联的策略信息
     *
     * @param projectId 项目唯一标识
     * @return StrategyUsageInfoDO 策略详情对象，若未找到则返回 null
     */
    private StrategyUsageInfoDO getStrategyInfo(String projectId) {
        // 1. 参数校验，如果任一为空则无法定位策略，直接返回 null
        if (StringUtils.isAnyBlank(projectId)) {
            log.warn("getStrategyInfo failed: projectId is blank. projectId={}", projectId);
            return null;
        }

        try {
            // 2. 首先在 strategy_usage 表中查找映射关系
            // 该表记录了哪个项目下的哪张表使用了哪个 infoId
            Optional<StrategyUsageDO> usageOpt = usageRepository.findByProjectId(projectId);

            if (usageOpt.isPresent()) {
                String infoId = usageOpt.get().getInfoId();
                if (StringUtils.isBlank(infoId)) {
                    log.warn("Strategy usage found but infoId is empty for project={}", projectId);
                    return null;
                }

                // 3. 根据查到的 infoId 去 strategy_usage_info 表中查询具体的 7 个 JSON 策略字段
                return infoRepository.findById(infoId).orElseGet(() -> {
                    log.error("Strategy info record not found for infoId: {}", infoId);
                    return null;
                });
            } else {
                // 如果没有配置策略，说明该表不受策略管控，返回 null 让上层逻辑执行默认行为（全放行）
                log.debug("No strategy mapping found for project={}", projectId);
            }
        } catch (Exception e) {
            // 防止数据库异常导致整个组件列表接口挂掉，这里记录错误并返回 null
            log.error("Error occurred while fetching strategy info for project={}", projectId, e);
        }

        return null;
    }
    /**
     * 将 7 个 JSON 字段的所有算子开关合并为一个扁平的 Map
     */
    private Map<String, Boolean> parseAllStrategiesToFlatMap(StrategyUsageInfoDO info) {
        Map<String, Boolean> flatMap = new HashMap<>();
        if (info == null) return flatMap;

        // 定义 Jackson 的类型引用
        TypeReference<Map<String, Boolean>> typeRef = new TypeReference<>() {};

        // 逐一合并 7 个字段的内容
        mergeJsonToMap(flatMap, info.getDataPreparation(), typeRef);
        mergeJsonToMap(flatMap, info.getDataFiltering(), typeRef);
        mergeJsonToMap(flatMap, info.getFeatureProcessing(), typeRef);
        mergeJsonToMap(flatMap, info.getStatistics(), typeRef);
        mergeJsonToMap(flatMap, info.getModelTraining(), typeRef);
        mergeJsonToMap(flatMap, info.getModelPrediction(), typeRef);
        mergeJsonToMap(flatMap, info.getModelEvaluation(), typeRef);

        return flatMap;
    }

    /**
     * 辅助方法：解析 JSON 并合并到目标 Map 中
     */
    private void mergeJsonToMap(Map<String, Boolean> target, String json, TypeReference<Map<String, Boolean>> typeRef) {
        if (StringUtils.isNotBlank(json)) {
            try {
                // 使用 objectMapper.readValue 替代 JsonUtils.toObject
                Map<String, Boolean> data = objectMapper.readValue(json, typeRef);
                if (data != null) {
                    target.putAll(data);
                }
            } catch (Exception e) {
                log.error("Failed to parse strategy JSON: {}", json, e);
            }
        }
    }



    @Override
    public ComponentDef getComponent(ComponentKey key) {
        return batchGetComponent(List.of(key)).get(0);
    }

    @Override
    public List<ComponentDef> batchGetComponent(List<ComponentKey> keys) {
        List<ComponentDef> result = new ArrayList<>();
        Map<ComponentKey, ComponentDef> componentMap = new HashMap<>();
        components.stream().filter(compListDef -> !CollectionUtils.isEmpty(compListDef.getCompsList())).forEach(compListDef -> compListDef.getCompsList().forEach(componentDef -> componentMap.put(new ComponentKey(compListDef.getName(), componentDef.getDomain(), componentDef.getName()), componentDef)));
        if (!CollectionUtils.isEmpty(keys)) {
            keys.forEach(key -> {
                if (!componentMap.containsKey(key)) {
                    throw SecretpadException.of(GraphErrorCode.COMPONENT_NOT_EXISTS, key.toString());
                }
                result.add(componentMap.get(key));
            });
        }
        return result;
    }

    @Override
    public Object listComponentI18n() {
        Map<String, Map<String, Object>> config = new HashMap<>();
        Map<String, Object> secretpad = new HashMap<>();
        Map<String, Object> scql = new HashMap<>();
        try {
            File dir = ResourceUtils.getFile(i18nLocation);
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    String app = fileName.substring(0, fileName.lastIndexOf('.'));
                    String str = FileUtils.readFile2String(file);
                    Map<String, Object> content = JsonUtils.toJavaMap(str, Object.class);
                    if (!CollectionUtils.isEmpty(content)) {
                        if (app.equals(ComponentConstants.SECRETPAD)) {
                            secretpad = content;
                        }else if (app.equals(ComponentConstants.SCQL)){
                            scql = content;
                        } else {
                            config.put(app, content);
                        }
                    }
                }
                Map<String, Object> finalSecretpad = secretpad;
                Map<String, Object> finalScql = scql;
                config.keySet().forEach(k -> {
                    config.get(k).putAll(finalSecretpad);
                    config.get(ComponentConstants.SECRETFLOW).putAll(finalScql);
                });
            }
        } catch (IOException e) {
            throw SecretpadException.of(GraphErrorCode.COMPONENT_18N_ERROR, e);
        }
        config.keySet().forEach(key -> {
            Map<String, Object> map = config.get(key);
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String hide = key + "/" + entry.getKey();
                if (secretpadComponentConfig.getHide().contains(hide)) {
                    iterator.remove();
                }
            }
        });
        return config;
    }

    @Override
    public boolean isSecretpadComponent(GraphNodeInfo node) {
        Pipeline.NodeDef pipelineNodeDef = ComponentTools.getNodeDef(node.getNodeDef());
        String componentId = pipelineNodeDef.getDomain() + ComponentConstants.COMP_ID_DELIMITER + pipelineNodeDef.getName();
        return ComponentConstants.PAD_COMP.contains(componentId);
    }

    @Override
    public ComponentVersion listComponentVersion(String deployMode) {
        var version = switch (deployMode) {
            case MPC -> ComponentVersion.builder()
                    .secretpadImage(secretFlowVersionConfig.getSecretpadImage())
                    .secretflowImage(secretFlowVersionConfig.getSecretflowImage())
                    .secretflowServingImage(secretFlowVersionConfig.getSecretflowServingImage())
                    .kusciaImage(secretFlowVersionConfig.getKusciaImage())
                    .dataProxyImage(secretFlowVersionConfig.getDataProxyImage())
                    .scqlImage(secretFlowVersionConfig.getScqlImage())
                    .build();

            case TEE -> ComponentVersion.builder()
                    .teeDmImage(secretFlowVersionConfig.getTeeDmImage())
                    .teeAppImage(secretFlowVersionConfig.getTeeAppImage())
                    .capsuleManagerSimImage(secretFlowVersionConfig.getCapsuleManagerSimImage())
                    .build();

            case ALL_IN_ONE -> ComponentVersion.builder()
                    .teeDmImage(secretFlowVersionConfig.getTeeDmImage())
                    .teeAppImage(secretFlowVersionConfig.getTeeAppImage())
                    .capsuleManagerSimImage(secretFlowVersionConfig.getCapsuleManagerSimImage())
                    .secretpadImage(secretFlowVersionConfig.getSecretpadImage())
                    .secretflowServingImage(secretFlowVersionConfig.getSecretflowServingImage())
                    .kusciaImage(secretFlowVersionConfig.getKusciaImage())
                    .secretflowImage(secretFlowVersionConfig.getSecretflowImage())
                    .dataProxyImage(secretFlowVersionConfig.getDataProxyImage())
                    .scqlImage(secretFlowVersionConfig.getScqlImage())
                    .build();
            default -> null;
        };
        log.info("listALLINONEComponentVersion:{}", version);
        return version;
    }
}