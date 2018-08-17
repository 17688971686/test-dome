package cs.mobile.service;

import cs.common.utils.Validate;
import cs.domain.sys.User;
import cs.domain.sys.User_;
import cs.repository.repositoryImpl.sys.UserRepo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ldm on 2018/7/25 0025.
 */
@Service
public class UserSvcImpl implements UserSvc {
    @Autowired
    private UserRepo userRepo;

    @Override
    public User findByToken(String token) {
        Criteria criteria = userRepo.getExecutableCriteria();
        criteria.add(Restrictions.eq(User_.token.getName(),token));
        List<User> userList = criteria.list();
        if(Validate.isList(userList)){
            return userList.get(0);
        }
        return null;
    }
}
