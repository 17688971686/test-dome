package com.sn.framework.module.sys.service;

import com.sn.framework.module.sys.domain.OrgDept;

import java.util.List;

/**
 * ldm
 */
public interface IOrgDeptService {

    /**
     * 获取所有的部门组织信息
     * @return
     */
    List<OrgDept> findAll();



}