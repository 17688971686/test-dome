package com.sn.framework.module.sys.repo;

import com.sn.framework.core.repo.IRepository;
import com.sn.framework.module.sys.domain.Resource;
import com.sn.framework.module.sys.domain.User;

import java.util.List;
import java.util.Set;

/**
 * Description: 系统资源  数据操作接口
 * Author: tzg
 * Date: 2017/9/14 9:42
 */
public interface IResourceRepo extends IRepository<Resource, String> {

    /**
     * 查询用户权限码
     * @param user
     * @return
     */
    Set<String> findUserPermission(User user);

    /**
     * 查询用户菜单
     * @param user
     * @param status
     * @return
     */
    List<Resource> findMenus(User user, String status);

}
