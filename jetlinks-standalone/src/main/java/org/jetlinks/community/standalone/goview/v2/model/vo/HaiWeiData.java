package org.jetlinks.community.standalone.goview.v2.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yangtao
 * @since 2024-07-30
 */
@TableName("t_haiwei_data")
@Data
public class HaiWeiData implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String name;
    /**
     * 账号
     */
    private String account;
    /**
     * 云数据中心的项目私钥
     */
    private String privateKey;
    /**
     * 机械码
     */
    private String machineCode;
    /**
     * 平台代码 1:浏览器、2:APP、7:小程序
     */
    private String platform;
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

}
