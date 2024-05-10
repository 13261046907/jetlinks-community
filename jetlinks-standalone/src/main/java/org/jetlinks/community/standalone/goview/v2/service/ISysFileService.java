package org.jetlinks.community.standalone.goview.v2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jetlinks.community.standalone.goview.v2.model.SysFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fc
 * @since 2022-12-22
 */
public interface ISysFileService extends IService<SysFile> {


	public SysFile selectByExamplefileName(String filename);
}
