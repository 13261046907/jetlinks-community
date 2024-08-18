package org.jetlinks.community.standalone.goview.v2.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetlinks.community.standalone.goview.v2.common.base.BaseController;
import org.jetlinks.community.standalone.goview.v2.common.domain.ResultTable;
import org.jetlinks.community.standalone.goview.v2.model.vo.UserDimensionVo;
import org.jetlinks.community.standalone.goview.v2.service.DimensionUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/dimensionUser")
@Tag(name = "用户维度关联表管理")
public class DimensionUserController extends BaseController {

    @Resource
    private DimensionUserService dimensionUserService;

    @ApiOperation(value = "获取用户维度", notes = "获取用户维度")
    @GetMapping("/list")
    @ResponseBody
    public ResultTable list(String userId){
        UserDimensionVo userDimensionVo = dimensionUserService.selecDimensionByUserId(userId);
        ResultTable resultTable=new ResultTable();
        resultTable.setData(userDimensionVo);
        resultTable.setCode(200);
        resultTable.setMsg("获取成功");
        return resultTable;
    }
}
