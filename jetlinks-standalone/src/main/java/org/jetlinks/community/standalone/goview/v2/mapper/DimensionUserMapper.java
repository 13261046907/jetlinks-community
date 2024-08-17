package org.jetlinks.community.standalone.goview.v2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jetlinks.community.standalone.goview.v2.model.DimensionUser;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fc
 * @since 2023-04-30
 */
public interface DimensionUserMapper extends BaseMapper<DimensionUser> {

    String selectByUserId(@Param("userId") String userId);

}
