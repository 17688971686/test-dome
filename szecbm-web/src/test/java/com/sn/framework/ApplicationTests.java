package com.sn.framework;

import com.sn.framework.module.sys.service.IDictService;
import com.sn.framework.module.sys.service.IOrganService;
import com.sn.framework.module.sys.service.IResourceService;
import com.sn.framework.module.sys.service.ISysService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ApplicationTests {

    @Autowired
    private IResourceService resourceService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IOrganService organService;

//    @Test
    public void initResourceData() throws Exception {
        resourceService.resetResource();
    }

//    @Test
    public void initDictData() throws Exception {
        dictService.initDictData();
    }

//    @Test
    public void initOrganData() throws Exception {
        organService.initOrganData();
    }

}
