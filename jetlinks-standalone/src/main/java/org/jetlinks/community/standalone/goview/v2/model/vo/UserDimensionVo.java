package org.jetlinks.community.standalone.goview.v2.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDimensionVo implements Serializable {
    private String userIds;
    private String roleIds;
    private String orgIds;
}
