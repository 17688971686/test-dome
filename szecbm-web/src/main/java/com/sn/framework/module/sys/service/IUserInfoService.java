package com.sn.framework.module.sys.service;


import com.sn.framework.module.sys.domain.User;

/**
 * Description: 用户信息  业务操作接口
 * @Author: tzg
 * @Date: 2017/7/11 18:41
 */
public interface IUserInfoService {

    /**
     * 通过登录名查询记录
     * @param username
     * @return
     */
    User findByUsername(String username);

//    void update(User record);

    /**
     * 更新登录成功的用户信息
     * @param userId
     */
    void loginSuccessById(String userId);

    /**
     * 更新登录成功的用户信息
     * @param username
     */
    void loginSuccess(String username);

    /**
     * 更新登录失败的用户信息
     * @param userId
     */
    void loginFailureById(String userId);

    /**
     * 更新登录失败的用户信息
     * @param username
     */
    void loginFailure(String username);

    /**
     * 通过用户
     * @param userId
     * @return
     */
    User getById(String userId);

    /**
     * 获取当前用户权限
     * @return
     */
//    Set<String> getCurrentUserPermissions();

    /**
     * 获取当前用户角色
     * @return
     */
//    Set<String> getCurrentUserRoles();


}