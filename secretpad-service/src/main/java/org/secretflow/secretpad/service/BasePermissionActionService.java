package org.secretflow.secretpad.service;

import org.secretflow.secretpad.service.model.basepermission.BasePermissionCheckRequest;

import java.io.InputStream;

/**
 * 基础权限动作执行服务 (查看、复制、下载)
 */
public interface BasePermissionActionService {

    /**
     * 查看数据预览 (采样前 N 行)
     * @param request 包含项目、节点、数据ID上下文
     * @return 采样的数据内容
     */
    Object viewData(BasePermissionCheckRequest request);

    /**
     * 复制数据 (读取全文或允许的最大数据量)
     * @param request 包含项目、节点、数据ID上下文
     * @return 完整数据内容文本
     */
    String copyData(BasePermissionCheckRequest request);

    /**
     * 下载数据 (启动流式文件传输)
     * @param request 包含项目、节点、数据ID上下文
     * @return 下载信息 (包含 InputStream 和文件名)
     */
    InputStream downloadData(BasePermissionCheckRequest request);
}
