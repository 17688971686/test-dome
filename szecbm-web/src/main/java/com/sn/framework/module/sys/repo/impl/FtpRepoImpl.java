package com.sn.framework.module.sys.repo.impl;

import com.sn.framework.core.repo.impl.AbstractRepository;
import com.sn.framework.module.sys.domain.Ftp;
import com.sn.framework.module.sys.domain.Ftp_;
import com.sn.framework.module.sys.domain.SysFile;
import com.sn.framework.module.sys.domain.SysFile_;
import com.sn.framework.module.sys.repo.IFtpRepo;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 * Created by ldm on 2018/7/11 0011.
 */
@Repository
public class FtpRepoImpl extends AbstractRepository<Ftp, String> implements IFtpRepo {

    @Override
    public Ftp getByIP(String enabledIP) {
        CriteriaQuery<Ftp> query = createCriteriaQuery();
        Root<Ftp> root = query.from(Ftp.class);

        Path ipPath = root.get(Ftp_.ipAddr);
        query.where(getBuilder().and(getBuilder().equal(ipPath, enabledIP)));

        return entityManager.createQuery(query.select(root)).getSingleResult();
    }
}
