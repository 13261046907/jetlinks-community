package org.jetlinks.community.device.entity;

import lombok.Getter;
import lombok.Setter;
import org.hswebframework.ezorm.rdb.mapping.annotation.Comment;
import org.hswebframework.web.api.crud.entity.GenericEntity;
import org.hswebframework.web.api.crud.entity.RecordCreationEntity;
import org.hswebframework.web.api.crud.entity.RecordModifierEntity;
import org.hswebframework.web.crud.annotation.EnableEntityEvent;

import javax.persistence.Table;

@Table(name = "dev_device_instance_template")
@Comment("设备信息模版表")
@EnableEntityEvent
@Getter
@Setter
public class DeviceInstanceTemplateEntity extends GenericEntity<String> {

    private String id;

    /**
     * 设备类型
     */
    private Integer device_type;

    private String device_id;

    private String topic;

    /**
     * 设备地址
     */
    private String device_address;

    /**
     * 功能码
     */
    private String function_code;

    /**
     * 寄存器地址
     */
    private String register_address;

    /**
     * 数据长度
     */
    private String data_length;

    /**
     * 指令
     */
    private String instruction;

    /**
     * 开指令
     */
    private String open_instruction;

    /**
     * 关指令
     */
    private String close_instruction;

    private String status;

    /**
     * 采集公式
     */
    private String collect_formula;

    /**
     * 控制公式
     */
    private String control_formula;

    /**
     * 采集频率
     */
    private String sampling_frequency;

    /**
     * 标题
     */
    private String title;

    private String create_time;

    private String create_userId;
}
