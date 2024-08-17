package org.jetlinks.community.standalone.goview.v2.model;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("s_dimension_user")
@Data
public class DimensionUser implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;

    private String dimensionTypeId;

    private String dimensionId;

    private String dimensionName;

    private String userId;

    private String userName;

    private String relation;

    private String relationName;

    private String features;

}
