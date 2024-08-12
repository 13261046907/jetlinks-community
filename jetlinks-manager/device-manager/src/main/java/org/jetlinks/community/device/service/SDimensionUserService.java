package org.jetlinks.community.device.service;

import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.jetlinks.community.device.entity.SDimensionUserEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class SDimensionUserService extends GenericReactiveCrudService<SDimensionUserEntity, String> {

    public Mono<List<SDimensionUserEntity>> findByUserId(String userId) {
        return createQuery()
            .where(SDimensionUserEntity::getUserId, userId)
            .and(SDimensionUserEntity::getDimensionTypeId, "org").fetch()
            .collectList();
    }

    public Mono<List<SDimensionUserEntity>> findByDimensionId(String dimensionId) {
        return createQuery()
            .where(SDimensionUserEntity::getDimensionId, dimensionId)
            .and(SDimensionUserEntity::getDimensionTypeId, "org").fetch()
            .collectList();
    }
}
