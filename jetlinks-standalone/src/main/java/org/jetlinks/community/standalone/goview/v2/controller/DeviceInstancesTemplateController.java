package org.jetlinks.community.standalone.goview.v2.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.jetlinks.community.standalone.enums.TaskEnum;
import org.jetlinks.community.standalone.goview.v2.common.base.BaseController;
import org.jetlinks.community.standalone.goview.v2.common.domain.AjaxResult;
import org.jetlinks.community.standalone.goview.v2.common.domain.ResultTable;
import org.jetlinks.community.standalone.goview.v2.model.DeviceInstancesTemplate;
import org.jetlinks.community.standalone.goview.v2.model.vo.DeviceVo;
import org.jetlinks.community.standalone.goview.v2.service.DeviceInstancesTemplateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
        deviceInstancesTemplate = extracted(deviceInstancesTemplate);
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
        deviceInstancesTemplate = extracted(deviceInstancesTemplate);
        Boolean b= deviceInstancesTemplateService.updateById(deviceInstancesTemplate);
        if(b){
            return success();
        }
        return error();
    }

    @PostMapping("/updateStateByDeviceId")
    @ResponseBody
    public AjaxResult updateStateByDeviceId(@RequestBody DeviceVo deviceVo)
    {
        deviceInstancesTemplateService.updateStateByDeviceId(deviceVo.getDeviceId(),deviceVo.getState());
        return success();
    }

    private DeviceInstancesTemplate extracted(DeviceInstancesTemplate deviceInstancesTemplate) {
        deviceInstancesTemplate.setCreateTime(DateUtil.now());
        //指令拼接
        String deviceAddress = deviceInstancesTemplate.getDeviceAddress();
        String functionCode = deviceInstancesTemplate.getFunctionCode();
        String openInstruction = "";
        String closeInstruction = "";
        String instruction = "";
        if(deviceInstancesTemplate.getDeviceType() == 2){
            //开关
            openInstruction = deviceAddress + functionCode + deviceInstancesTemplate.getOpenInstruction();
            closeInstruction = deviceAddress + functionCode + deviceInstancesTemplate.getCloseInstruction();
            deviceInstancesTemplate.setOpenInstruction(openInstruction);
            deviceInstancesTemplate.setCloseInstruction(closeInstruction);
        }else {
            //属性
            instruction = deviceAddress + functionCode + deviceInstancesTemplate.getInstruction();
            deviceInstancesTemplate.setInstruction(instruction);
        }
        //采集时间
        String samplingFrequency = deviceInstancesTemplate.getSamplingFrequency();
        String cron = "";
        if(StringUtils.isNotBlank(samplingFrequency)){
            TaskEnum taskKey = TaskEnum.getTaskKey(samplingFrequency);
            if(!Objects.isNull(taskKey)){
                cron = taskKey.getKey();
            }
        }
        deviceInstancesTemplate.setSamplingFrequency(cron);
        return deviceInstancesTemplate;
    }
}
