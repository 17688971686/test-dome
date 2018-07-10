package com.sn.framework.module.sys.repo.impl;

import com.sn.framework.common.StringUtil;
import com.sn.framework.core.repo.impl.AbstractRepository;
import com.sn.framework.module.sys.domain.Dict;
import com.sn.framework.module.sys.domain.Dict_;
import com.sn.framework.module.sys.repo.IDictRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Description: 数据字典  数据操作实现类
 * Author: qbl
 * Date: 2017/9/12 17:09
 */
@Repository
public class DictRepoImpl extends AbstractRepository<Dict, String> implements IDictRepo {

    private final Logger logger = LoggerFactory.getLogger(OrganRepoImpl.class);

    @Override
    public List<Dict> findChildrenById(String dictId) {
        CriteriaQuery<Dict> query = createCriteriaQuery();
        Root<Dict> root = query.from(Dict.class);
        Path parentIdPath = root.get(Dict_.parentId),
                itemOrderPath = root.get(Dict_.itemOrder);
        Predicate pp;
        if (StringUtil.isBlank(dictId)) {
            pp = parentIdPath.isNull();
        } else {
            pp = getBuilder().equal(parentIdPath, dictId);
        }
        query.where(
                getBuilder().and(
                        pp
                )
        );
        query.orderBy(getBuilder().asc(itemOrderPath));
        try {
            return entityManager.createQuery(query.select(root)).getResultList();
        } catch (NoResultException nre) {
            logger.warn("未找到数据");
            return null;
        }
    }

    @Override
    public List<Dict> findBeforeDicts(String parentId, Integer itemOrder) {
        CriteriaQuery<Dict> query = createCriteriaQuery();
        Root<Dict> root = query.from(Dict.class);
        Predicate pp;
        Path parentIdPath = root.get(Dict_.parentId),
                itemOrderPath = root.get(Dict_.itemOrder);
        if (StringUtil.isBlank(parentId)) {
            pp = parentIdPath.isNull();
        } else {
            pp = getBuilder().equal(parentIdPath, parentId);
        }
        query.where(
                getBuilder().and(
                        pp,
                        getBuilder().lessThan(itemOrderPath, itemOrder == null ? 0 : itemOrder)
                )
        ).orderBy(getBuilder().asc(itemOrderPath));
        try {
            return entityManager.createQuery(query.select(root)).getResultList();
        } catch (NoResultException nre) {
            logger.warn("未找到数据");
            return null;
        }
    }

    @Override
    public List<Dict> findAfterDicts(String parentId, Integer itemOrder) {
        CriteriaQuery<Dict> query = createCriteriaQuery();
        Root<Dict> root = query.from(Dict.class);
        Path parentIdPath = root.get(Dict_.parentId),
                itemOrderPath = root.get(Dict_.itemOrder);
        Predicate pp;
        if (StringUtil.isBlank(parentId)) {
            pp = parentIdPath.isNull();
        } else {
            pp = getBuilder().equal(parentIdPath, parentId);
        }
        query.where(
                getBuilder().and(
                        pp,
                        getBuilder().greaterThan(itemOrderPath, itemOrder == null ? 0 : itemOrder)
                )
        );
        query.orderBy(getBuilder().asc(itemOrderPath));
        try {
            return entityManager.createQuery(query.select(root)).getResultList();
        } catch (NoResultException nre) {
            logger.warn("未找到数据");
            return null;
        }
    }

    @Override
    public Dict findByParentIdAndDictKey(String parentId, String dictKey) {
        CriteriaQuery<Dict> query = createCriteriaQuery();
        Root<Dict> root = query.from(Dict.class);
        query.where(getBuilder().and(
                getBuilder().equal(root.get(Dict_.parentId), parentId),
                getBuilder().equal(root.get(Dict_.dictKey), dictKey)
        ));
        try {
            return entityManager.createQuery(query.select(root)).getSingleResult();
        } catch (NoResultException nre) {
            logger.warn("未找到数据");
            return null;
        }
    }
}