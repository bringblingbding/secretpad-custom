package org.secretflow.secretpad.service.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据容量与单位换算工具类
 */
public class DataUnitConverter {

    /**
     * 将带有单位的值统一转换为 Byte 用于底层精准比较。
     * 支持 KB, MB, GB。如果不属于能转换为 bytes 的容量单位（比如 ROWS 或空），则直接返回原始值。
     *
     * @param value 原始数量
     * @param unit 单位字符串 (ROWS, KB, MB, GB, 等)
     * @return 转换后的值
     */
    public static long convertToBytesIfCapacity(long value, String unit) {
        if (StringUtils.isBlank(unit)) {
            return value;
        }

        switch (unit.trim().toUpperCase()) {
            case "KB":
                return value * 1024L;
            case "MB":
                return value * 1024L * 1024L;
            case "GB":
                return value * 1024L * 1024L * 1024L;
            case "TB":
                return value * 1024L * 1024L * 1024L * 1024L;
            case "B":
            case "BYTE":
            case "BYTES":
                return value;
            case "ROWS":
            default:
                // 如果是按 ROWS 或不支持的单位算数量，直接返回原始值进行绝对值比较
                return value;
        }
    }

    /**
     * 校验请求的单位和限制的单位是否属于同一个比较体系 (ROWS 只能和 ROWS 比较，容量只能互相比较)
     */
    public static boolean isSameSystem(String requestUnit, String limitUnit) {
        if (StringUtils.isBlank(requestUnit) || StringUtils.isBlank(limitUnit)) {
            // 没有传单位，或者都没有配置，认为同源
            return true;
        }
        boolean reqIsRow = "ROWS".equalsIgnoreCase(requestUnit.trim());
        boolean limitIsRow = "ROWS".equalsIgnoreCase(limitUnit.trim());
        
        return reqIsRow == limitIsRow;
    }
}
