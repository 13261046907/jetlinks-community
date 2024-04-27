package org.jetlinks.community.standalone.view.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jetlinks.community.standalone.view.model.SysFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fc
 * @since 2022-12-22
 */
public interface ISysFileService extends IService<SysFile> {


    SysFile selectByExamplefileName(String filename);
}
