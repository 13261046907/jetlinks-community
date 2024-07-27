package org.jetlinks.community.standalone.goview.v2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetlinks.community.standalone.goview.v2.mapper.DeviceInstancesTemplateMapper;
import org.jetlinks.community.standalone.goview.v2.model.DeviceInstancesTemplate;
import org.jetlinks.community.standalone.goview.v2.service.DeviceInstancesTemplateService;
import org.springframework.stereotype.Service;

@Service("iDeviceInstancesTemplateService")
public class DeviceInstancesTemplateServiceImpl extends ServiceImpl<DeviceInstancesTemplateMapper, DeviceInstancesTemplate> implements DeviceInstancesTemplateService {
}
