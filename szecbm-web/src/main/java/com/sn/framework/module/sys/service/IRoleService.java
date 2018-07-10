package com.sn.framework.module.sys.service;


import com.sn.framework.core.service.ISService;
import com.sn.framework.module.sys.domain.Resource;
import com.sn.framework.module.sys.model.RoleDto;

import java.util.List;
import java.util.Set;

/**
 * Description: 角色信息  业务处理接口
 * @Author: tzg
 * @Date: 2017/8/25 10:49
 */
public interface IRoleService extends ISService<RoleDto> {

    /**
     * 启用
     * @param roleId
     */
    void enable(String roleId);

    /**
     * 禁用
     * @param roleId
     */
    void disable(String roleId);

    /**
     * 为角色授权
     * @param roleId
     * @param resources
     */
    void authorization(String roleId, Set<Resource> resources);

    /**
     * 查询用户角色数据
     * @param userId
     * @return
     */
    List<RoleDto> findUserRoles(String userId);
}