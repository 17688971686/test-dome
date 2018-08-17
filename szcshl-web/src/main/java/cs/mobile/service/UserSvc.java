package cs.mobile.service;

import cs.domain.sys.User;

/**
 * Created by Administrator on 2018/7/25 0025.
 */
public interface UserSvc {

    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    User findByToken(String token);
}
