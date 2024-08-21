package org.jetlinks.community.device.service;

import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.jetlinks.community.device.configuration.DbUtils;
import org.jetlinks.community.device.entity.DeviceInstanceTemplateEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DeviceInstanceTemplateService extends GenericReactiveCrudService<DeviceInstanceTemplateEntity, String> {

    public Mono<DeviceInstanceTemplateEntity> findByDeviceId(String deviceId) {
        return createQuery()
            .where(DeviceInstanceTemplateEntity::getDeviceId, deviceId).fetchOne();
    }

    public Mono<DeviceInstanceTemplateEntity> findByInstruction(String topic,String instruction) {
        return createQuery()
            .where(DeviceInstanceTemplateEntity::getSendTopic, topic)
            .and(DeviceInstanceTemplateEntity::getCloseInstructionCrc,instruction)
            .or(DeviceInstanceTemplateEntity::getSendTopic,topic)
            .and(DeviceInstanceTemplateEntity::getOpenInstructionCrc, instruction)
            .or(DeviceInstanceTemplateEntity::getAcceptTopic,topic)
            .and(DeviceInstanceTemplateEntity::getCloseInstructionCrc, instruction)
            .or(DeviceInstanceTemplateEntity::getAcceptTopic,topic)
            .and(DeviceInstanceTemplateEntity::getOpenInstructionCrc, instruction)
            .or(DeviceInstanceTemplateEntity::getSendTopic, topic)
            .and(DeviceInstanceTemplateEntity::getInstructionCrc, instruction)
            .or(DeviceInstanceTemplateEntity::getAcceptTopic, topic)
            .and(DeviceInstanceTemplateEntity::getInstructionCrc, instruction)
            .fetchOne();
    }

    public void updateStatusById(String status,String id) {
        String sql = "update dev_device_instance_template set status ="+status+" where id="+id;
        log.info("executeSQL:{}:",sql);
        DbUtils.execute(sql);
    }
}
