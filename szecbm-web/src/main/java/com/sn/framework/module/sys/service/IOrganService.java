package com.sn.framework.module.sys.service;

import com.sn.framework.core.service.ISService;
import com.sn.framework.module.sys.domain.Resource;
import com.sn.framework.module.sys.model.OrganDto;

import java.util.List;
import java.util.Set;

/**
 *
 * @author qbl
 * @date 2017/9/7
 */
public interface IOrganService extends ISService<OrganDto> {


    /**
     * 初始化机构数据
     * @throws Exception
     */
    void initOrganData() throws Exception;

    /**
     * 为机构授权
     * @param organId
     * @param resources
     */
    void authorization(String organId, Set<Resource> resources);

    /**
     * 查询业主机构列表
     * @return
     */
    List<OrganDto> findOrganList();
}
