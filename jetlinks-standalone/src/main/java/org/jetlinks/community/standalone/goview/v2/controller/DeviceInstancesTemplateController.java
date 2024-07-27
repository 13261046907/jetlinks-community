package org.jetlinks.community.standalone.goview.v2.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetlinks.community.standalone.goview.v2.common.base.BaseController;
import org.jetlinks.community.standalone.goview.v2.common.domain.AjaxResult;
import org.jetlinks.community.standalone.goview.v2.common.domain.ResultTable;
import org.jetlinks.community.standalone.goview.v2.model.DeviceInstancesTemplate;
import org.jetlinks.community.standalone.goview.v2.service.DeviceInstancesTemplateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/template")
@Tag(name = "设备模版管理")
public class DeviceInstancesTemplateController extends BaseController {

    @Resource
    private DeviceInstancesTemplateService deviceInstancesTemplateService;

    @ApiOperation(value = "获取列表", notes = "获取列表")
    @GetMapping("/list")
    @ResponseBody
    public ResultTable list(String deviceId){
        LambdaQueryWrapper<DeviceInstancesTemplate> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(DeviceInstancesTemplate::getDeviceId,deviceId);
        List<DeviceInstancesTemplate> list = deviceInstancesTemplateService.list(objectLambdaQueryWrapper);
        ResultTable resultTable=new ResultTable();
        resultTable.setData(list);
        resultTable.setCode(200);
        resultTable.setMsg("获取成功");
        return resultTable;
    }

    /**
     * 新增保存
     * @param
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult add(@RequestBody DeviceInstancesTemplate deviceInstancesTemplate){
        deviceInstancesTemplate.setCreateTime(DateUtil.now());
        boolean b=deviceInstancesTemplateService.save(deviceInstancesTemplate);
        if(b){
            return successData(200, deviceInstancesTemplate).put("msg", "创建成功");
        }else{
            return error();
        }
    }

    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult edit(@RequestBody DeviceInstancesTemplate deviceInstancesTemplate)
    {
        Boolean b= deviceInstancesTemplateService.updateById(deviceInstancesTemplate);
        if(b){
            return success();
        }
        return error();
    }
}
