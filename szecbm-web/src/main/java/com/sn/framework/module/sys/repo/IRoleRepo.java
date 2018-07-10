package com.sn.framework.module.sys.repo;


import com.sn.framework.core.repo.IRepository;
import com.sn.framework.module.sys.domain.Role;
import com.sn.framework.module.sys.model.RoleDto;

import java.util.List;

/**
 * Description: 角色信息   数据操作接口
 * @Author: lyc
 * @Date: 2017/8/24 15:30
 */
public interface IRoleRepo extends IRepository<Role, String> {

    /**
     * 通过角色名称查询角色信息
     * @param roleName
     * @return
     */
    Role findByRoleName(String roleName);

    /**
     * 查询用户角色信息列表
     * @param userId
     * @return
     */
    List<RoleDto> findUserRoles(String userId);
}
