package org.jetlinks.community.device.entity;

import lombok.Getter;
import lombok.Setter;
import org.hswebframework.ezorm.rdb.mapping.annotation.Comment;
import org.hswebframework.web.crud.annotation.EnableEntityEvent;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "s_dimension_user")
@Comment("用户维度关联表")
@Getter
@Setter
@EnableEntityEvent
public class SDimensionUserEntity{

    @Column(name = "id")
    private String id;

    /**
     * 维度类型ID
     */
    @Column(name = "dimension_type_id")
    private String dimensionTypeId;

    @Column(name = "dimension_id")
    private String dimensionId;

    @Column(name = "dimension_name")
    private String dimensionName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "relation")
    private String relation;

    @Column(name = "relation_name")
    private String relationName;

    @Column(name = "features")
    private BigDecimal features;

}
