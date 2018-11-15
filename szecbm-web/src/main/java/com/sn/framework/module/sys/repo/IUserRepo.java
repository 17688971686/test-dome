package com.sn.framework.module.sys.repo;


import com.sn.framework.core.repo.IRepository;
import com.sn.framework.module.sys.domain.User;

import java.util.List;
import java.util.Set;

/**
 * 用户信息  数据操作接口
 * Created by lqs on 2017/7/19.
 */
public interface IUserRepo extends IRepository<User, String> {

    /**
     * 通过登录名查询用户信息
     *
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 查询用户权限码
     *
     * @param username
     * @return
     */
    Set<String> getUserPermission(String username);

    /**
     * 获取用户角色码
     *
     * @param username
     * @return
     */
    Set<String> getUserRoles(String username);

    /**
     * 用户资源
     * @param username
     * @param status
     * @return
     */
//    List<Resource> getUserResources(String username, String status);

    /**
     * 查询角色为市领导的用户信息
     * @return
     */
    List<User> findMajorList();

    /**
     * 根据机构查询部门下所有人的手机号码
     * @param organId
     * @return
     */
    List<String> getUserMobileNumberByOrganId(String organId);

    /**
     * 根据部门ID查询用户信息
     * @param organId
     * @return
     */
    List<User> getUserByOrganId(String organId,boolean queryAll);

    /**
     * 查询所有有部门的用户
     * @return
     */
    List<User> getOrganUser();
}
