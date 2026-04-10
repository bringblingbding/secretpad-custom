package org.secretflow.secretpad.service;

import org.secretflow.secretpad.service.model.basepermission.BasePermissionCheckRequest;

/**
 * 基础权限校验服务接口
 * 负责对 view（查看）、copy（复制）、download（下载）三类操作做策略合规校验
 */
public interface BasePermissionCheckService {

    /**
     * 对指定项目下的基础权限动作进行合规校验
     * 若任意一个动作不满足策略限制，立即抛出 SecretpadException 阻断调用链
     *
     * @param request 包含项目 ID 和动作列表的请求对象
     */
    void check(BasePermissionCheckRequest request);
}
