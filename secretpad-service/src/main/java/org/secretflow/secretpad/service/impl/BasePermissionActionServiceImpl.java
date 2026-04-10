package org.secretflow.secretpad.service.impl;

import org.secretflow.secretpad.common.errorcode.DataErrorCode;
import org.secretflow.secretpad.common.exception.SecretpadException;
import org.secretflow.secretpad.manager.integration.node.AbstractNodeManager;
import org.secretflow.secretpad.manager.integration.model.NodeResultDTO;
import org.secretflow.secretpad.service.BasePermissionActionService;
import org.secretflow.secretpad.service.BasePermissionCheckService;
import org.secretflow.secretpad.service.model.basepermission.BasePermissionCheckRequest;
import org.secretflow.secretpad.service.model.data.DownloadInfo;
import org.secretflow.secretpad.service.DataService;
import org.secretflow.secretpad.service.model.data.DownloadDataRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础权限动作执行服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BasePermissionActionServiceImpl implements BasePermissionActionService {

    private final BasePermissionCheckService basePermissionCheckService;
    private final DataService dataService;

    @Override
    public Object viewData(BasePermissionCheckRequest request) {
        // 1. 获取元数据（包含长度）
        DownloadInfo info = getDownloadInfo(request);
        
        // 2. 注入校验信息 (View 动作一般不强制容量校验，但可为后续预留)
        injectRequestValue(request, info.getFileLength(), "BYTES");
        basePermissionCheckService.check(request);
        
        // 3. 读取采样数据 (前 100 行)
        return readLines(info.getInputStream(), 100);
    }

    @Override
    public String copyData(BasePermissionCheckRequest request) {
        // 1. 获取元数据
        DownloadInfo info = getDownloadInfo(request);
        
        // 2. 注入校验信息
        injectRequestValue(request, info.getFileLength(), "BYTES");
        basePermissionCheckService.check(request);
        
        // 3. 读取全文
        return readFullText(info.getInputStream());
    }

    @Override
    public InputStream downloadData(BasePermissionCheckRequest request) {
        // 1. 获取元数据
        DownloadInfo info = getDownloadInfo(request);
        
        // 2. 注入校验信息：关键动作，确保下载量被核销
        injectRequestValue(request, info.getFileLength(), "BYTES");
        basePermissionCheckService.check(request);
        
        // 3. 返回流
        return info.getInputStream();
    }

    private DownloadInfo getDownloadInfo(BasePermissionCheckRequest request) {
        DownloadDataRequest downloadRequest = DownloadDataRequest.builder()
                .nodeId(request.getNodeId())
                .domainDataId(request.getDomainDataId())
                .build();
        return dataService.download(downloadRequest);
    }

    private void injectRequestValue(BasePermissionCheckRequest request, long value, String unit) {
        if (request.getActions() != null) {
            for (BasePermissionCheckRequest.ActionItem action : request.getActions()) {
                if (action.getRequestValue() == null) {
                    action.setRequestValue(value);
                    action.setRequestUnit(unit);
                }
            }
        }
    }

    private List<String> readLines(InputStream is, int limit) {
        List<String> lines = new ArrayList<>();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null && lines.size() < limit) {
                lines.add(line);
            }
        } catch (Exception e) {
            log.error("Read lines error", e);
        }
        return lines;
    }

    private String readFullText(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            log.error("Read full text error", e);
        }
        return sb.toString();
    }
}
