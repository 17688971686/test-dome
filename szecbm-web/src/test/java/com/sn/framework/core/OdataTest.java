package com.sn.framework.core;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sn.framework.module.sys.domain.User;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * Description: odata测试用例
 * Author: tzg
 * Date: 2017/7/26 14:37
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class OdataTest {

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
    public void findUserByOdata() throws Exception {
        OdataJPA odata = new OdataJPA("username eq 'admin' and substringof('ad',username)",
                new String[]{"itemOrder asc"}, true, 0, 10);
        odata.addFilter("+roles.roleName", "eq", "ADMIN");
        odata.addOrderby("username", true);
        List<User> userList = odata.criteriaQuery(entityManager, User.class).getResultList();

        Assert.assertNotNull("未查询到用户数据", userList);

        System.out.println();
        System.out.println("===>>  count: " + odata.getCount() + " list: " + objectMapper.writeValueAsString(userList));
        System.out.println();
    }

//    @Test
    public void findUserByCriteria() throws Exception {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        Join<User, ?> roleJoin = root.join("roles", JoinType.LEFT);
        query.where(builder.equal(roleJoin.get("roleName"), "ADMIN"));
        List<User> userList = entityManager.createQuery(query.select(root)).getResultList();

        Assert.assertNotNull("未查询到用户数据", userList);

        System.out.println();
        System.out.println("===>> list: " + objectMapper.writeValueAsString(userList));
        System.out.println();
    }

}