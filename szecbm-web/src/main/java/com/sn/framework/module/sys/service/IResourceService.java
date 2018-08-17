package com.sn.framework.module.sys.service;

import com.sn.framework.core.service.ISService;
import com.sn.framework.module.sys.domain.Resource;

/**
 * Description: 系统资源  业务处理接口
 * @Author: tzg
 * @Date: 2017/9/14 9:47
 */
public interface IResourceService extends ISService<Resource> {

    /**
     * 初始化系统资源数据
     * @throws Exception
     */
    void initResourceData() throws Exception;

    /**
     * 重置系统资源
     */
    void resetResource() throws Exception;

}
