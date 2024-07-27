package org.jetlinks.community.standalone.goview.v2.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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

    private String deviceId;

    /**
     * 指令
     */
    private String instruction;

    /**
     * 标题
     */
    private String title;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    private String createUserId;



}
