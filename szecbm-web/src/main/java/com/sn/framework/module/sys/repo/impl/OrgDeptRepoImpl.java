package com.sn.framework.module.sys.repo.impl;

import com.sn.framework.core.repo.impl.AbstractRepository;
import com.sn.framework.module.sys.domain.OrgDept;
import com.sn.framework.module.sys.repo.IOrgDeptRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * Created by lqs on 2017/7/19.
 */
@Repository
public class OrgDeptRepoImpl extends AbstractRepository<OrgDept, String> implements IOrgDeptRepo {

    private final Logger logger = LoggerFactory.getLogger(OrgDeptRepoImpl.class);

}
