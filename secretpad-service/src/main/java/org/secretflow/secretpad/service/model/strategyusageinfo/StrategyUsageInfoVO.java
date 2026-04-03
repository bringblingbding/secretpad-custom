package org.secretflow.secretpad.service.model.strategyusageinfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.secretflow.secretpad.common.util.JsonUtils;
import org.secretflow.secretpad.persistence.entity.StrategyUsageInfoDO;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * Strategy Usage Info view object
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyUsageInfoVO {

    @Schema(description = "策略内容的id")
    private String infoId;

    @Schema(description = "起始时间")
    private LocalDateTime validStartTime;

    @Schema(description = "结束时间")
    private LocalDateTime validEndTime;

    @Schema(description = "可使用的星期几")
    private String timeWindowDays;

    @Schema(description = "每天开始时间")
    private String dailyStartTime;

    @Schema(description = "每天结束时间")
    private String dailyEndTime;

    @Schema(description = "地点")
    private String regionLimit;

    @Schema(description = "限定位置")
    private String positionLimit;

    @Schema(description = "运行环境")
    private String envRequirements;

    @Schema(description = "主体")
    private String allowedUsers;

    @Schema(description = "交付连接器")
    private String deliveryConnectors;

    @Schema(description = "使用连接器")
    private String usageConnectors;

    @Schema(description = "限定角色")
    private String roleLimit;

    @Schema(description = "数据状态")
    private String requiredDataState;

    @Schema(description = "自动销毁")
    private Boolean isAutoDestroy;

    @Schema(description = "VPN")
    private Boolean isVpnRequired;

    @Schema(description = "传输协议")
    private String allowedProtocols;

    @Schema(description = "加密信道")
    private String encryptionChannel;

    @Schema(description = "存储加密")
    private Boolean isStorageEncrypted;

    @Schema(description = "加密算法")
    private String storageEncAlgo;

    @Schema(description = "存储位置")
    private String storageLocations;

    @Schema(description = "保留时间")
    private Integer retentionDays;


    @Schema(description = "查看限制配置")
    private String view;

    @Schema(description = "复制限制配置")
    private String copy;

    @Schema(description = "下载限制配置")
    private String download;

    @Schema(description = "交付方式")
    private String deliveryType;

    @Schema(description = "直接交付配置")
    private String directDelivery;

    @Schema(description = "不出域交付配置")
    private String internalDelivery;

    @Schema(description = "出域交付配置")
    private String externalDelivery;

    @Schema(description = "应用管控")
    private String applicationControls;

    @Schema(description = "流转控制")
    private String flowControl;

    // --- 用途限制 ---

    @Schema(description = "履行义务")
    private String complianceObligations;

    @Schema(description = "数据准备")
    private String dataPreparation;

    @Schema(description = "数据过滤")
    private String dataFiltering;

    @Schema(description = "特征处理")
    private String featureProcessing;

    @Schema(description = "统计")
    private String statistics;

    @Schema(description = "模型训练")
    private String modelTraining;

    @Schema(description = "模型预测")
    private String modelPrediction;

    @Schema(description = "模型评估")
    private String modelEvaluation;

    /**
     * 将 DO 转换为 VO
     *
     * @param infoDO 数据库实体
     * @return 视图对象
     */
    public static StrategyUsageInfoVO from(StrategyUsageInfoDO infoDO) {
        if (infoDO == null) {
            return null;
        }

        StrategyUsageInfoVO vo = new StrategyUsageInfoVO();
        // 优化点：因为字段名完全一致，直接 Copy 即可，无需手写大量 Setter
        // 如果需要特殊处理（如 Enum 转 String），再在 copyProperties 后单独 Set
        BeanUtils.copyProperties(infoDO, vo);
        return vo;
    }

}