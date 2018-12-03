package com.sn.framework.module.sys.repo;

import com.sn.framework.core.repo.IRepository;
import com.sn.framework.module.sys.domain.Organ;

import java.util.List;

/**
 * 机构信息  数据操作接口
 * @author qbl
 * @date 2017/9/7
 */
public interface IOrganRepo extends IRepository<Organ,String>{

    /**
     * 通过机构名称查询机构信息
     * @param organName
     * @return
     */
    Organ findByOrganName(String organName);

    /**
     * 通过机构 ID 获取机构名称
     * @param organId
     * @return
     */
    String findOrganNameById(String organId);

    /**
     *
     * @return
     */
    List<Organ> findOrganList();

    /**
     *
     * @param jurisdiction
     * @return
     */
    Organ findJurisdicon(String jurisdiction);

    /**
     *获取主任机构信息
     * @param organName
     * @return
     */
    List<Organ> findByOrganByOrgMLeader(String organName);

    /**
     * 获取分管领导机构信息
     * @param organName
     * @return
     */
    List<Organ> findByOrganByOrganManage(String organName);

    /**
     * 获取部长记过机构信息
     * @param organName
     * @return
     */
    List<Organ> findByOrganByOrganLead(String organName);

}
