package org.secretflow.secretpad.service.model.strategyusageengine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 策略引擎校验执行结果与注入参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyUsageExecutionResult {
    /**
     * 校验是否通过（如果不抛出异常走到了最后，通常为 true）
     */
    private boolean isPassed;

    /**
     * 注入的义务或安全参数配置
     * 例如: "setEncrypt" -> true, "setMask" -> true
     */
    private Map<String, Object> injectedParams;
}
