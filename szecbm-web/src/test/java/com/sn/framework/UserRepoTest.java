package com.sn.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sn.framework.common.SnRuntimeException;
import com.sn.framework.common.StringUtil;
import com.sn.framework.module.sys.domain.User_;
import com.sn.framework.module.sys.repo.IUserRepo;
import com.sn.framework.odata.impl.jpa.OdataJPADelete;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static com.sn.framework.core.Constants.USER_KEY_ADMIN;

/**
 * Description: 用户信息 业务操作测试类
 * Author: tzg
 * Date: 2017/8/27 21:55
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class UserRepoTest {

    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private ObjectMapper objectMapper;


    //    @Test
    public void findByUserRoleName() throws JsonProcessingException {
        Set<String> roleNames = userRepo.getUserRoles(USER_KEY_ADMIN);

        Assert.assertNotNull("未查询到用户角色数据", roleNames);

        System.out.println();
        System.out.println("===>>  " + objectMapper.writeValueAsString(roleNames));
        System.out.println();
    }


    @Test
//    @Transactional
    public void getUserPermission() throws JsonProcessingException {
        Set<String> permissions = userRepo.getUserPermission(USER_KEY_ADMIN);

        Assert.assertNotNull("未查询到用户资源数据", permissions);

        System.out.println();
        System.out.println("===>>  size: " + permissions.size() + "   data: " + objectMapper.writeValueAsString(permissions));
        System.out.println();
        permissions.forEach(x -> {
            if (StringUtil.isBlank(x)) {
                throw new SnRuntimeException("存在空字符");
            }
        });
    }

//    @Test
//    @Transactional
    public void deleteByOdata() {
        OdataJPADelete odata = new OdataJPADelete();
        int rn = userRepo.deleteByOdata((OdataJPADelete) odata.addEQFilter(User_.username, USER_KEY_ADMIN));
        System.out.println("===>> delete number: " + rn);
    }

}