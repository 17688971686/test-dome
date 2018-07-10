package com.sn.framework.module.sys.service.impl;

import com.sn.framework.module.sys.domain.User;
import com.sn.framework.module.sys.repo.IUserRepo;
import com.sn.framework.module.sys.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * Description: 用户信息  业务操作接口
 * @Author: tzg
 * @Date: 2017/7/11 18:41
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    private IUserRepo userRepo;

    @Override
    public User findByUsername(String loginName) {
        return userRepo.findByUsername(loginName);
    }

    @Override
    public User getById(String userId) {
        return userRepo.getById(userId);
    }

    public void update(User record) {
        record.setLastLoginDate(new Date());
        userRepo.save(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void loginSuccessById(String userId) {
        User m = getById(userId);
        m.setLoginFailCount(0);
        update(m);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void loginSuccess(String username) {
        User m = findByUsername(username);
        m.setLoginFailCount(0);
        update(m);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void loginFailureById(String userId) {
        User m = getById(userId);
        m.setLoginFailCount(m.getLoginFailCount() + 1);
        update(m);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void loginFailure(String username) {
        User m = findByUsername(username);
        m.setLoginFailCount(m.getLoginFailCount() + 1);
        update(m);
    }

    //    @Override
//    @Transactional(readOnly = true)
//    public Set<String> getCurrentUserPermissions() {
//        return userRepo.getUserPermission(SessionUtil.getUsername());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Set<String> getCurrentUserRoles() {
//        return userRepo.getUserRoles(SessionUtil.getUsername());
//    }


}