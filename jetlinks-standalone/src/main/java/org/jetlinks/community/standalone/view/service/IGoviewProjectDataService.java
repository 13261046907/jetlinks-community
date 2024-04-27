package org.jetlinks.community.standalone.view.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jetlinks.community.standalone.view.model.GoviewProjectData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fc
 * @since 2023-04-30
 */
public interface IGoviewProjectDataService extends IService<GoviewProjectData> {

    GoviewProjectData getProjectid(String projectId);

}
