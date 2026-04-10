package org.secretflow.secretpad.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.secretflow.secretpad.service.BasePermissionCheckService;
import org.secretflow.secretpad.service.model.basepermission.BasePermissionCheckRequest;
import org.secretflow.secretpad.service.model.common.SecretPadResponse;
import org.springframework.web.bind.annotation.*;

/**
 * 基础权限校验控制器
 * 提供前端页面「查看」「复制」「下载」按钮触发时的策略合规校验接口
 */
@RestController
@RequestMapping("/api/v1alpha1/strategy/permission")
@Tag(name = "base-permission-controller", description = "数据基础权限（查看/复制/下载）校验接口")
@RequiredArgsConstructor
public class BasePermissionCheckController {

    private final BasePermissionCheckService basePermissionCheckService;

    /**
     * 校验基础权限
     * 前端在用户点击查看/复制/下载按钮时调用此接口
     * 若校验不通过会直接抛出异常并返回错误信息
     */
    @PostMapping("/check")
    @ResponseBody
    @Operation(
            summary = "基础权限校验",
            description = "对 view/copy/download 操作做策略合规校验。" +
                    "支持同时传入多个动作进行联合校验，任意一项不满足均会阻断。"
    )
    public SecretPadResponse<String> check(@RequestBody @Valid BasePermissionCheckRequest request) {
        basePermissionCheckService.check(request);
        return SecretPadResponse.success("basic permission check passed");
    }
}
