package org.jetlinks.community.standalone.goview.v2.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author fc
 * @since 2023-04-30
 */
@TableName("dev_device_instance_template")
@Data
public class DeviceInstancesTemplate implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private String id;


    /**
     * 设备类型
     */
    private Integer deviceType;

    private String deviceId;

    private String sendTopic;

    private String acceptTopic;

    /**
     * 设备地址
     */
    private String deviceAddress;

    /**
     * 功能码
     */
    private String functionCode;

    /**
     * 寄存器地址
     */
    private String registerAddress;

    /**
     * 数据长度
     */
    private String dataLength;

    /**
     * 指令
     */
    private String instruction;

    /**
     * 开指令
     */
    private String openInstruction;

    /**
     * 关指令
     */
    private String closeInstruction;

    private String status;

    /**
     * 采集公式
     */
    private String collectFormula;

    /**
     * 控制公式
     */
    private String controlFormula;

    /**
     * 采集频率
     */
    private String samplingFrequency;

    /**
     * 采集频率
     */
    @TableField(exist = false)
    private String samplingFrequencyName;

    /**
     * 标题
     */
    private String title;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    private String createUserId;



}
