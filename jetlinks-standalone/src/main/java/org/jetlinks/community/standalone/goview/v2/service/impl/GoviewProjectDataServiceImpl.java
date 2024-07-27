package org.jetlinks.community.standalone.goview.v2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetlinks.community.standalone.goview.v2.mapper.GoViewProjectDataMapper;
import org.jetlinks.community.standalone.goview.v2.model.GoviewProjectData;
import org.jetlinks.community.standalone.goview.v2.service.IGoviewProjectDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fc
 * @since 2023-04-30
 */
@Service("iGoviewProjectDataService")
public class GoviewProjectDataServiceImpl extends ServiceImpl<GoViewProjectDataMapper, GoviewProjectData> implements IGoviewProjectDataService {
	@Resource
    GoViewProjectDataMapper dataMapper;
	@Override
	public GoviewProjectData getProjectid(String projectId) {
		LambdaQueryWrapper<GoviewProjectData> lambdaQueryWrapper=new LambdaQueryWrapper<GoviewProjectData>();
		lambdaQueryWrapper.eq(GoviewProjectData::getProjectId, projectId);
		return dataMapper.selectOne(lambdaQueryWrapper);

	}

}
