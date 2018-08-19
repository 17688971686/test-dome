package com.sn.framework.module.sys.repo.impl;

import com.sn.framework.core.common.Validate;
import com.sn.framework.core.repo.impl.AbstractRepository;
import com.sn.framework.module.sys.domain.Organ;
import com.sn.framework.module.sys.domain.Organ_;
import com.sn.framework.module.sys.domain.User;
import com.sn.framework.module.sys.domain.User_;
import com.sn.framework.module.sys.repo.IResourceRepo;
import com.sn.framework.module.sys.repo.IUserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by lqs on 2017/7/19.
 */
@Repository
public class UserRepoImpl extends AbstractRepository<User, String> implements IUserRepo {

    private final Logger logger = LoggerFactory.getLogger(UserRepoImpl.class);

    @Autowired
    private IResourceRepo resourceRepo;

    @Override
    public User findByUsername(String username) {
        CriteriaQuery<User> query = createCriteriaQuery();
        Root<User> root = query.from(User.class);
        query.where(getBuilder().equal(root.get(User_.username.getName()), username));
        try {
            return entityManager.createQuery(query.select(root)).getSingleResult();
        } catch (NoResultException nre) {
            logger.warn("未找到【" + username + "】");
        }
        return null;
    }

    @Override
    @Transactional
    public Set<String> getUserPermission(String username) {
//        String hql = "SELECT rs.permCode FROM User u INNER JOIN u.roles r INNER JOIN r.resources rs WHERE u.username=? AND (rs.permCode IS NOT NULL OR rs.permCode <> '')";
//        Query query = entityManager.createQuery(hql);
//        query.setParameter(1, username);
//        List<String> permCodes = query.getResultList();
//        return new HashSet<>(permCodes);
        return resourceRepo.findUserPermission(findByUsername(username));
    }

    @Override
    public Set<String> getUserRoles(String username) {
        Query query = entityManager.createQuery("SELECT r.roleName FROM User u INNER JOIN u.roles r WHERE u.username= ?");
        List<String> roleNames = query.setParameter(1, username).getResultList();
        return new HashSet<>(roleNames);
    }

    @Override
    public List<User> findMajorList() {
        Query query = entityManager.createQuery("FROM User u where u.userType='3'");
        List<User> majors = query.getResultList();
        return majors;
    }

    @Override
    public List<String> getUserMobileNumberByOrganId(String organId) {
        CriteriaQuery<String> query = getBuilder().createQuery(String.class);
        Root<User> root = query.from(User.class);
        Join<User, Organ> organJoin = root.join(User_.organ);
        query.where(
                getBuilder().and(
                        getBuilder().equal(organJoin.get(Organ_.organId), organId),
                        getBuilder().isNotNull(root.get(User_.mobileNumber))
                )
        );
        try {
            return entityManager.createQuery(query.select(root.get(User_.mobileNumber))).getResultList();
        } catch (NoResultException nre) {
            logger.warn("未找到【{}】", organId);
        }
        return null;
    }

    @Override
    public List<User> getUserByOrganId(String organId, boolean queryAll) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        Predicate predicate = null;
        if (Validate.isString(organId)) {
            Join<User, ?> organJoin = root.join("organ", JoinType.LEFT);
            predicate = builder.and(builder.equal(organJoin.get(Organ_.organId.getName()), organId));
        }
        if (queryAll) {
            if (null == predicate) {
                predicate = builder.and(builder.equal(root.get(User_.useState.getName()), "1"));
            } else {
                predicate = builder.and(builder.equal(root.get(User_.useState.getName()), "1"), predicate);
            }
        }
        query.where(predicate);
        List<User> userList = entityManager.createQuery(query.select(root)).getResultList();
        return userList;
    }


}
