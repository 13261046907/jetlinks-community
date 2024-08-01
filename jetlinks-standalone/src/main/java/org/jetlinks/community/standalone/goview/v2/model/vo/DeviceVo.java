package org.jetlinks.community.standalone.goview.v2.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceVo implements Serializable {
    private String deviceId;
    private String state;
}
