package org.jetlinks.community.standalone.goview.v2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.jetlinks.community.standalone.goview.v2.mapper.DimensionUserMapper;
import org.jetlinks.community.standalone.goview.v2.model.DimensionUser;
import org.jetlinks.community.standalone.goview.v2.model.vo.UserDimensionVo;
import org.jetlinks.community.standalone.goview.v2.service.DimensionUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service("iDimensionUserService")
public class DimensionUserServiceImpl extends ServiceImpl<DimensionUserMapper, DimensionUser> implements DimensionUserService {

    @Resource
    private  DimensionUserMapper dimensionUserMapper;

    public UserDimensionVo selecDimensionByUserId(String userId) {
        UserDimensionVo userDimensionVo = new UserDimensionVo();
        List<String> userList = dimensionUserMapper.selectUserIdByUserId(userId);
        if(CollectionUtils.isNotEmpty(userList)){
            String userIds = userList.stream().collect(Collectors.joining(", "));
            userDimensionVo.setUserIds(userIds);
        }
        List<String> orgList = dimensionUserMapper.selectOrgIdByUserId(userId);
        if(CollectionUtils.isNotEmpty(orgList)){
            String orgIds = orgList.stream().collect(Collectors.joining(", "));
            userDimensionVo.setOrgIds(orgIds);
        }
        List<String> roleList = dimensionUserMapper.selectRoleByUserId(userId);
        if(CollectionUtils.isNotEmpty(roleList)){
            String roseIds = roleList.stream().collect(Collectors.joining(", "));
            userDimensionVo.setRoleIds(roseIds);
        }
        return userDimensionVo;
    }

}