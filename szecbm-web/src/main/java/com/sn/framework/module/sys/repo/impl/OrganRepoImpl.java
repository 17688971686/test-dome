package com.sn.framework.module.sys.repo.impl;

import com.sn.framework.core.repo.impl.AbstractRepository;
import com.sn.framework.module.sys.domain.Organ;
import com.sn.framework.module.sys.domain.Organ_;
import com.sn.framework.module.sys.repo.IOrganRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by qbl on 2017/9/7.
 */
@Repository
public class OrganRepoImpl extends AbstractRepository<Organ, String> implements IOrganRepo {

    private final Logger logger = LoggerFactory.getLogger(OrganRepoImpl.class);

    @Override
    public Organ findByOrganName(String organName) {
        CriteriaQuery<Organ> query = createCriteriaQuery();
        Root<Organ> root = query.from(Organ.class);
        query.where(getBuilder().equal(root.get(Organ_.organName), organName));
        try {
            return entityManager.createQuery(query.select(root)).getSingleResult();
        } catch (NoResultException nre) {
            logger.warn("未找到【{}】", organName);
        }
        return null;
    }

    @Override
    public String findOrganNameById(String organId) {
        CriteriaQuery<String> query = getBuilder().createQuery(String.class);
        Root<Organ> root = query.from(Organ.class);
        query.where(getBuilder().equal(root.get(Organ_.organId), organId));
        try {
            return entityManager.createQuery(query.select(root.get(Organ_.organName))).getSingleResult();
        } catch (NoResultException nre) {
            logger.warn("未找到【{}】", organId);
        }
        return null;
    }

    @Override
    public List<Organ> findOrganList() {
        String hql = "SELECT new Organ(organId, organName, parentId) FROM Organ o WHERE o.parentId='GXBSYZDW' " +
                " OR o.parentId='GXBSXBDW' OR o.parentId='GXBSXQZF' OR o.parentId='GXBSXQFGW' " +
                " ORDER BY o.parentId,o.itemOrder";
        return entityManager.createQuery(hql).getResultList();
    }

    @Override
    public Organ findJurisdicon(String jurisdiction) {
        CriteriaQuery<Organ> query = createCriteriaQuery();
        Root<Organ> root = query.from(Organ.class);
        query.where(getBuilder().and(
                getBuilder().equal(root.get(Organ_.parentId), "GXBSXQFGW"),
                getBuilder().equal(root.get(Organ_.organRegion), jurisdiction)
        ));
        try {
            return entityManager.createQuery(query.select(root)).getSingleResult();
        } catch (NoResultException nre) {
            logger.warn("未找到【" + jurisdiction + "】");
        }
        return null;
    }
}
