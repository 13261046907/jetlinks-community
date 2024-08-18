package org.jetlinks.community.standalone.goview.v2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jetlinks.community.standalone.goview.v2.model.DimensionUser;
import org.jetlinks.community.standalone.goview.v2.model.vo.UserDimensionVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fc
 * @since 2023-04-30
 */
public interface DimensionUserService extends IService<DimensionUser> {
    UserDimensionVo selecDimensionByUserId(String userId);
}
