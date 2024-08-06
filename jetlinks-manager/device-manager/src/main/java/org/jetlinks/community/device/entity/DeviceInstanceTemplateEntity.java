package org.jetlinks.community.device.entity;

import lombok.Getter;
import lombok.Setter;
import org.hswebframework.ezorm.rdb.mapping.annotation.Comment;
import org.hswebframework.web.api.crud.entity.GenericEntity;
import org.hswebframework.web.crud.annotation.EnableEntityEvent;

import javax.persistence.Column;
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
    @Column(name = "device_type")
    private Integer deviceType;

    @Column(name = "device_id")
    private String deviceId;

    private String topic;

    /**
     * 设备地址
     */
    @Column(name = "device_address")
    private String deviceAddress;

    /**
     * 功能码
     */
    @Column(name = "function_code")
    private String functionCode;

    /**
     * 寄存器地址
     */
    @Column(name = "register_address")
    private String registerAddress;

    /**
     * 数据长度
     */
    @Column(name = "data_length")
    private String dataLength;

    /**
     * 指令
     */
    private String instruction;

    /**
     * 开指令
     */
    @Column(name = "open_instruction")
    private String openInstruction;

    /**
     * 关指令
     */
    @Column(name = "deviceclose_instruction_id")
    private String closeInstruction;

    private String status;

    /**
     * 采集公式
     */
    @Column(name = "collect_formula")
    private String collectFormula;

    /**
     * 控制公式
     */
    @Column(name = "control_formula")
    private String controlFormula;

    /**
     * 采集频率
     */
    @Column(name = "sampling_frequency")
    private String samplingFrequency;

    /**
     * 标题
     */
    private String title;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "create_userId")
    private String createUserId;
}
