package org.jetlinks.community.standalone.goview.v2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetlinks.community.standalone.goview.v2.mapper.GoviewProjectMapper;
import org.jetlinks.community.standalone.goview.v2.model.GoviewProject;
import org.jetlinks.community.standalone.goview.v2.service.IGoviewProjectService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fc
 * @since 2023-04-30
 */
@Service("iGoviewProjectService")
public class GoviewProjectServiceImpl extends ServiceImpl<GoviewProjectMapper, GoviewProject> implements IGoviewProjectService {

}
