package com.sn.framework.module.sys.repo.impl;

import com.sn.framework.core.common.ResultMsg;
import com.sn.framework.core.repo.impl.AbstractRepository;
import com.sn.framework.module.sys.domain.SysFile;
import com.sn.framework.module.sys.domain.SysFile_;
import com.sn.framework.module.sys.repo.ISysFileRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by ldm on 2018/7/11 0011.
 */
@Repository
public class SysFileRepoImpl extends AbstractRepository<SysFile, String> implements ISysFileRepo {

    private final Logger logger = LoggerFactory.getLogger(SysFileRepoImpl.class);

    @Override
    public List<SysFile> queryFileList(String mainId, String sysBusiType) {
        CriteriaQuery<SysFile> query = createCriteriaQuery();
        Root<SysFile> root = query.from(SysFile.class);
        Path mainIdPath = root.get(SysFile_.mainId),
             sysBusiTypePath = root.get(SysFile_.sysBusiType),
             sortPath = root.get(SysFile_.sort);
        query.where(getBuilder().and(
                getBuilder().equal(mainIdPath, mainId),
                getBuilder().equal(sysBusiTypePath, sysBusiType)
        ));
        query.orderBy(getBuilder().asc(sortPath));

        return entityManager.createQuery(query.select(root)).getResultList();
    }

    @Override
    public List<SysFile> findByMainId(String mainId) {
        CriteriaQuery<SysFile> query = createCriteriaQuery();
        Root<SysFile> root = query.from(SysFile.class);
        Path parentIdPath = root.get(SysFile_.mainId),
                sortPath = root.get(SysFile_.sort);
        Predicate pp = getBuilder().equal(parentIdPath, mainId);
        query.where(getBuilder().and(pp));
        query.orderBy(getBuilder().asc(sortPath));

        return entityManager.createQuery(query.select(root)).getResultList();
    }

    @Override
    public List<SysFile> findByBusinessId(String businessId) {
        CriteriaQuery<SysFile> query = createCriteriaQuery();
        Root<SysFile> root = query.from(SysFile.class);
        Path businessIdPath = root.get(SysFile_.businessId),
             sortPath = root.get(SysFile_.sort);
        Predicate pp = getBuilder().equal(businessIdPath, businessId);
        query.where(getBuilder().and(pp));
        query.orderBy(getBuilder().asc(sortPath));

        return entityManager.createQuery(query.select(root)).getResultList();
    }

    @Override
    public void delete(String mainId, String businessId, String sysBusiType, String showName) {

    }

    @Override
    public ResultMsg deleteByFileId(String sysFileId) {
        return null;
    }

    @Override
    public SysFile isExistFile(String fileUrl, String fileName) {
        return null;
    }
}
