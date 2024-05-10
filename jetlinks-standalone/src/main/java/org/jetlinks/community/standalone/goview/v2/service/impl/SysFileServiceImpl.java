package org.jetlinks.community.standalone.goview.v2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetlinks.community.standalone.goview.v2.mapper.SysFileMapper;
import org.jetlinks.community.standalone.goview.v2.model.SysFile;
import org.jetlinks.community.standalone.goview.v2.service.ISysFileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fc
 * @since 2022-12-22
 */
@Service("iSysFileService")
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements ISysFileService {
	@Resource
	private SysFileMapper sysFileMapper;

	@Override
	public SysFile selectByExamplefileName(String filename) {
		SysFile sysFile=sysFileMapper.selectOne(new LambdaQueryWrapper<SysFile>().eq(SysFile::getFileName, filename));
        return sysFile;
	}

}
