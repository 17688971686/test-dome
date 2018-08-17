package com.sn.framework.module.sys.repo.impl;

import com.sn.framework.core.repo.impl.AbstractRepository;
import com.sn.framework.module.sys.domain.SysVariable;
import com.sn.framework.module.sys.domain.SysVariable_;
import com.sn.framework.module.sys.repo.ISysVariableRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Description: 系统变量信息   数据操作实现类
 *
 * @author: tzg
 * @date: 2018/1/23 20:24
 */
@Repository
public class SysVariableRepoImpl extends AbstractRepository<SysVariable, String> implements ISysVariableRepo {
    private final Logger logger = LoggerFactory.getLogger(SysVariableRepoImpl.class);

    @Override
    public SysVariable findByCode(String varCode) {
        CriteriaQuery<SysVariable> query = createCriteriaQuery();
        Root<SysVariable> root = query.from(SysVariable.class);
        query.where(getBuilder().equal(root.get(SysVariable_.varCode), varCode));
        try {
            return entityManager.createQuery(query.select(root)).getSingleResult();
        } catch (NoResultException nre) {
            logger.warn("未找到【{}】", varCode);
            return null;
        }
    }

}