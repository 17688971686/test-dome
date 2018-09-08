package cs.mobile.service;

import cs.common.utils.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by ldm on 2018/7/25 0025.
 */
@Service
public class UserSvcImpl implements UserSvc {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Object findByToken(String token) {
        List<Map<String, Object>> userList = jdbcTemplate.queryForList("select * from cs_user where token = '"+token+"' ");
        if(Validate.isList(userList)){
            return userList.get(0);
        }
        return null;
    }

}
