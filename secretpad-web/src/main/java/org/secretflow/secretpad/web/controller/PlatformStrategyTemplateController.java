package org.secretflow.secretpad.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.secretflow.secretpad.common.annotation.resource.ApiResource;
import org.secretflow.secretpad.common.constant.resource.ApiResourceCodeConstants;
import org.secretflow.secretpad.common.util.PageUtils;
//import org.secretflow.secretpad.persistence.entity.PlatformConnectorDO;
import org.secretflow.secretpad.persistence.entity.PlatformStrategyTemplateDO;
import org.secretflow.secretpad.service.PlatformStrategyTemplateService;
import org.secretflow.secretpad.service.model.common.SecretPadResponse;
//import org.secretflow.secretpad.service.model.platformconnector.AllPlatformConnectorListVO;
import org.secretflow.secretpad.service.model.platformstrategytemplate.AllPlatformStrategyTemplateListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1alpha1/platformStrategyTemplate")
public class PlatformStrategyTemplateController {
    @Autowired
    private PlatformStrategyTemplateService platformStrategyTemplateService;

    @ResponseBody
    @PostMapping(value = "/create", consumes = "application/json")
    @ApiResource(code = ApiResourceCodeConstants.DATATABLE_LIST)
    @Tag(name = "platform-strategy-template-controller",description = "返回值为该任务的查询记录")
    public SecretPadResponse<PlatformStrategyTemplateDO> insert(@RequestBody @Valid PlatformStrategyTemplateDO request) {
        platformStrategyTemplateService.insert(request);
        PlatformStrategyTemplateDO platformStrategyTemplateDO = new PlatformStrategyTemplateDO();
        platformStrategyTemplateDO.setStrategyId(request.getStrategyId());
        return SecretPadResponse.success(platformStrategyTemplateService.select(platformStrategyTemplateDO).get(0));
    }

    @ResponseBody
    @PostMapping(value = "/selectByPage", consumes = "application/json")
    @ApiResource(code = ApiResourceCodeConstants.DATATABLE_LIST)
    @Tag(name = "platform-strategy-template-controller",description = "分页查询 要查询的字段就填 ")
    public SecretPadResponse<AllPlatformStrategyTemplateListVO> selectByPage(@RequestBody @Valid PlatformStrategyTemplateDO request) {
        List<PlatformStrategyTemplateDO> lists = platformStrategyTemplateService.select(request);
        int pageNum = request.getPageNum() != null ? request.getPageNum() : 1;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 10;
        List<PlatformStrategyTemplateDO> pages = PageUtils.rangeList(lists, pageSize, pageNum);
        AllPlatformStrategyTemplateListVO allPlatformStrategyTemplateListVO = new AllPlatformStrategyTemplateListVO();
        allPlatformStrategyTemplateListVO.setPlatformStrategyTemplateVOList(pages);
        allPlatformStrategyTemplateListVO.setPlatformStrategyTemplateNums(pages.size());

        return SecretPadResponse.success(allPlatformStrategyTemplateListVO);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = "application/json")
    @ApiResource(code = ApiResourceCodeConstants.DATATABLE_LIST)
    @Tag(name = "platform-strategy-template-controller",description = "返回全部结果")
    public SecretPadResponse<AllPlatformStrategyTemplateListVO> list(@RequestBody @Valid PlatformStrategyTemplateDO request) {
        AllPlatformStrategyTemplateListVO allPlatformStrategyTemplateListVO = new AllPlatformStrategyTemplateListVO();
        List<PlatformStrategyTemplateDO> lists = platformStrategyTemplateService.select(request);
        allPlatformStrategyTemplateListVO.setPlatformStrategyTemplateVOList(lists);
        allPlatformStrategyTemplateListVO.setPlatformStrategyTemplateNums(lists.size());
        return SecretPadResponse.success(allPlatformStrategyTemplateListVO);
    }

    @ResponseBody
    @PostMapping(value = "/update", consumes = "application/json")
    @ApiResource(code = ApiResourceCodeConstants.DATATABLE_LIST)
    @Tag(name = "platform-strategy-template-controller",description = "更新 返回更新后的结果")
    public SecretPadResponse<PlatformStrategyTemplateDO> update(@RequestBody @Valid PlatformStrategyTemplateDO request) {
        platformStrategyTemplateService.update(request);

        PlatformStrategyTemplateDO platformStrategyTemplateDO = new PlatformStrategyTemplateDO();
        platformStrategyTemplateDO.setStrategyId(request.getStrategyId());

        return SecretPadResponse.success(platformStrategyTemplateService.select(platformStrategyTemplateDO).get(0));
    }

    @ResponseBody
    @PostMapping(value = "/delete", consumes = "application/json")
    @ApiResource(code = ApiResourceCodeConstants.DATATABLE_LIST)
    @Tag(name = "platform-strategy-template-controller",description = "软删除")
    public SecretPadResponse delete(@RequestBody @Valid PlatformStrategyTemplateDO request) {
        platformStrategyTemplateService.delete(request);

        return SecretPadResponse.success();
    }
}
