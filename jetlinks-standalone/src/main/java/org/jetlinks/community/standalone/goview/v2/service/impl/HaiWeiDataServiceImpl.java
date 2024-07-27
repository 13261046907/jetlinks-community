package org.jetlinks.community.standalone.goview.v2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetlinks.community.standalone.goview.v2.mapper.HaiWeiDataMapper;
import org.jetlinks.community.standalone.goview.v2.model.HaiWeiData;
import org.jetlinks.community.standalone.goview.v2.service.IHaiWeiDataService;
import org.springframework.stereotype.Service;

@Service("iHaiWeiDataService")
public class HaiWeiDataServiceImpl extends ServiceImpl<HaiWeiDataMapper, HaiWeiData> implements IHaiWeiDataService {
}
