package org.secretflow.secretpad.common.errorcode;

/**
 * Usage Control Error Codes
 * 策略引擎相关的错误码定义
 */
public enum StrategyUsageControlErrorCode implements ErrorCode {

    /**
     * 策略检查未通过 (通用拦截)
     * 使用时，将具体拦截原因作为 args 传入
     */
    STRATEGY_CHECK_FAILED(202050001),

    /**
     * 未找到策略配置
     */
    STRATEGY_NOT_FOUND(202050002),

    /**
     * 策略配置无效 (如 JSON 解析失败)
     */
    STRATEGY_CONFIG_INVALID(202050003),

    /**
     * 缺少必要的请求参数
     */
    STRATEGY_PARAM_MISSING(202050004);

    private final int code;

    StrategyUsageControlErrorCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessageKey() {
        // 对应国际化资源文件中的 key，例如 usage.control.STRATEGY_CHECK_FAILED
        return "usage.control." + this.name();
    }

    @Override
    public Integer getCode() {
        return code;
    }
}