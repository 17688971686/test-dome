package com.sn.framework.module.sys.service;


import com.sn.framework.core.service.ISService;
import com.sn.framework.module.sys.domain.Resource;
import com.sn.framework.module.sys.domain.User;
import com.sn.framework.module.sys.model.UserDto;
import com.sn.framework.module.sys.model.UserInfoDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务接口
 *
 * @author lqs
 * @date 2017/7/19
 */
public interface IUserService extends ISService<UserDto> {


    /**
     * 根本用户状态
     * @param userId    用户ID
     * @param isEnable  是否启用
     */
    void changeUserState(String userId, boolean isEnable);

    /**
     * 通过用户名查找用户信息
     * @param username
     * @param ignoreProperties
     * @return
     */
    UserDto findByUsername(String username, String... ignoreProperties);

    /**
     * 获取用户资源
     * @param username
     * @param status
     * @return
     */
    List<Resource> getUserResources(String username, String status);

    /**
     * 修改当前用户基本信息
     * @param userDto
     */
    void saveCurrentUserInfo(UserInfoDto userDto);

    /**
     * 查询类型为市领导的人员
     */
    List<UserDto> findMajorList();

    /**
     * 重置用户密码
     * @param userId
     */
    void resetPwd(String userId);

    /**
     * 设置用户的角色
     * @param userId
     * @param roleIds
     */
    void setRoles(String userId, String roleIds);


    /**
     * 根据部门ID查询对应的用户信息
     */

     List<UserDto> findUserByOrgId();

    /**
     * 重置所有用户的账号密码
     */
    void resetAllUserPwd();

    /**
     * 查询部门用户
     * @return
     */
    List<UserDto> getOrganUser();
}
