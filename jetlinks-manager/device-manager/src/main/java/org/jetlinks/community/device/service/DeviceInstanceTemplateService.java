package org.jetlinks.community.device.service;

import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.jetlinks.community.device.entity.DeviceInstanceTemplateEntity;
import org.jetlinks.core.device.DeviceRegistry;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DeviceInstanceTemplateService extends GenericReactiveCrudService<DeviceInstanceTemplateEntity, String> {

    private final DeviceRegistry registry;


    public DeviceInstanceTemplateService(DeviceRegistry registry) {
        this.registry = registry;
    }


    public Mono<DeviceInstanceTemplateEntity> findByDeviceId(String deviceId) {
        return createQuery()
            .where(DeviceInstanceTemplateEntity::getDeviceId, deviceId).fetchOne();
    }

    public Mono<Integer> updateStatusById(String status,String deviceId) {
        return createUpdate()
            .set(DeviceInstanceTemplateEntity::getStatus, status)
            .where(DeviceInstanceTemplateEntity::getDeviceId, deviceId)
            .execute();
    }
}
