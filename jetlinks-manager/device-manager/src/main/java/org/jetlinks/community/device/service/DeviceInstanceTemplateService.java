package org.jetlinks.community.device.service;

import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.jetlinks.community.device.entity.DeviceInstanceTemplateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DeviceInstanceTemplateService extends GenericReactiveCrudService<DeviceInstanceTemplateEntity, String> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DeviceInstanceTemplateService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public Mono<DeviceInstanceTemplateEntity> findByDeviceId(String deviceId) {
        return createQuery()
            .where(DeviceInstanceTemplateEntity::getDeviceId, deviceId).fetchOne();
    }

    public Mono<DeviceInstanceTemplateEntity> findByInstruction(String instruction) {
        return createQuery()
            .where(DeviceInstanceTemplateEntity::getInstructionCrc, instruction)
            .or(DeviceInstanceTemplateEntity::getOpenInstructionCrc, instruction)
            .or(DeviceInstanceTemplateEntity::getCloseInstructionCrc, instruction).fetchOne();
    }

    public void updateStatusById(String status,String id) {
        String sql = "update dev_device_instance_template set status = "+ status + " where id= "+ id;
        jdbcTemplate.update(sql);
    }
}
