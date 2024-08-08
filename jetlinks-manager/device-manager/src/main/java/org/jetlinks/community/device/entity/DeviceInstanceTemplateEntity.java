package org.jetlinks.community.device.entity;

import lombok.Getter;
import lombok.Setter;
import org.hswebframework.ezorm.rdb.mapping.annotation.Comment;
import org.hswebframework.web.api.crud.entity.GenericEntity;
import org.hswebframework.web.crud.annotation.EnableEntityEvent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dev_device_instance_template")
@Comment("设备信息模版表")
@Getter
@Setter
@EnableEntityEvent
public class DeviceInstanceTemplateEntity extends GenericEntity<String> {

    @Id
    @Column(name = "id")
    private String id;

    /**
     * 设备类型
     */
    @Column(name = "device_type")
    private Integer deviceType;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "send_topic")
    private String sendTopic;

    @Column(name = "accept_topic")
    private String acceptTopic;

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
    @Column(name = "instruction_crc")
    private String instructionCrc;

    /**
     * 开指令
     */
    @Column(name = "open_instruction")
    private String openInstruction;
    @Column(name = "open_instruction_crc")
    private String openInstructionCrc;

    /**
     * 关指令
     */
    @Column(name = "close_instruction")
    private String closeInstruction;
    @Column(name = "close_instruction_crc")
    private String closeInstructionCrc;

    @Column(name = "status")
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
    @Column(name = "title")
    private String title;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "create_userId")
    private String createUserId;
}
