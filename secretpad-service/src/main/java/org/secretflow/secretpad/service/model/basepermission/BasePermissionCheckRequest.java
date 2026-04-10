package org.secretflow.secretpad.service.model.basepermission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 基础权限（查看/复制/下载）校验请求对象
 * 专供前端页面操作按钮触发时，调用策略引擎进行权限鉴定
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasePermissionCheckRequest {

    /**
     * 项目 ID，用于定位策略记录
     */
    @Schema(description = "项目ID", example = "project-001")
    private String projectId;

    /**
     * 当前操作用户 ID
     */
    @Schema(description = "用户ID", example = "yqtest")
    private String userId;

    /**
     * 当前用户角色
     */
    @Schema(description = "用户角色", example = "dataProvider")
    private String userRole;

    /**
     * 本次需要校验的动作列表（支持同时携带多个动作进行联合校验）
     * 例如 view + download 同时携带，任意一个不满足均会阻断
     */
    @Schema(description = "需要校验的操作列表")
    private List<ActionItem> actions;

    /**
     * 单个操作项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionItem {

        /**
         * 操作类型：view（查看）/ copy（复制）/ download（下载）
         */
        @Schema(description = "操作类型", example = "download", allowableValues = {"view", "copy", "download"})
        private String actionType;

        /**
         * 本次预计消耗的数据量数值（可选，用于对比 maxSingleLimit）
         */
        @Schema(description = "预计消耗量数值", example = "50")
        private Long requestValue;

        /**
         * 预计消耗量的单位：ROWS / KB / MB / GB（可选）
         */
        @Schema(description = "消耗量单位", example = "MB", allowableValues = {"ROWS", "KB", "MB", "GB"})
        private String requestUnit;
    }
}
