package org.secretflow.secretpad.web.controller;

import org.secretflow.secretpad.service.BasePermissionActionService;
import org.secretflow.secretpad.service.model.basepermission.BasePermissionCheckRequest;
import org.secretflow.secretpad.service.model.common.SecretPadResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 基础权限动作拦截控制器
 */
@RestController
@RequestMapping(value = "/api/v1alpha1/strategy/permission/action")
@RequiredArgsConstructor
public class BasePermissionActionController {

    private final BasePermissionActionService basePermissionActionService;

    /**
     * 查看数据预览 (经过策略拦截)
     */
    @PostMapping("/view")
    @Operation(summary = "查看数据预览", description = "在执行物理读取前自动进行使用策略核销")
    public SecretPadResponse<Object> viewData(@RequestBody @Valid BasePermissionCheckRequest request) {
        return SecretPadResponse.success(basePermissionActionService.viewData(request));
    }

    /**
     * 复制数据内容 (经过策略拦截)
     */
    @PostMapping("/copy")
    @Operation(summary = "复制数据内容", description = "用于前端 Ctrl+C 展示，执行前自动核销策略")
    public SecretPadResponse<String> copyData(@RequestBody @Valid BasePermissionCheckRequest request) {
        return SecretPadResponse.success(basePermissionActionService.copyData(request));
    }

    /**
     * 下载数据文件 (经过策略拦截)
     */
    @PostMapping("/download")
    @Operation(summary = "下载数据文件", description = "启动下载流，执行前核销容量上限、频率等策略")
    public void downloadData(@RequestBody @Valid BasePermissionCheckRequest request, HttpServletResponse response) {
        InputStream inputStream = basePermissionActionService.downloadData(request);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment;filename=" + request.getDomainDataId() + ".csv");
        
        try (OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException("Download failed", e);
        }
    }
}
