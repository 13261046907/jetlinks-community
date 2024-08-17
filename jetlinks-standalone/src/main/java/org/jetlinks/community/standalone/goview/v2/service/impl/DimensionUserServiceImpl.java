package org.jetlinks.community.standalone.goview.v2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetlinks.community.standalone.goview.v2.mapper.DimensionUserMapper;
import org.jetlinks.community.standalone.goview.v2.model.DimensionUser;
import org.jetlinks.community.standalone.goview.v2.service.DimensionUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("iDimensionUserService")
public class DimensionUserServiceImpl extends ServiceImpl<DimensionUserMapper, DimensionUser> implements DimensionUserService {

    @Resource
    private  DimensionUserMapper dimensionUserMapper;

    public String selectByUserId(String userId) {
        return dimensionUserMapper.selectByUserId(userId);
    }
}