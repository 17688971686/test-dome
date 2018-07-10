package com.sn.framework.module.sys.service.impl;

import com.sn.framework.module.sys.service.IOrganService;
import com.sn.framework.module.sys.service.IResourceService;
import com.sn.framework.module.sys.service.ISysService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description: 系统初始化数据
 * @Author: tzg
 * @Date: 2017/9/22 14:45
 */
@Service
public class SysInitFactory implements InitializingBean {

    @Autowired
    private ISysService sysService;
    @Autowired
    private IResourceService resourceService;
    @Autowired
    private IOrganService organService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void afterPropertiesSet() throws Exception {
        // 判断系统是否已初始化
        if (sysService.sysInit()) {

            organService.initOrganData();

            resourceService.resetResource();
        }
    }

}