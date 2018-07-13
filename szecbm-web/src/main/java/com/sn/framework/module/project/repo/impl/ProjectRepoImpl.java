package com.sn.framework.module.project.repo.impl;

import com.sn.framework.core.repo.impl.AbstractRepository;
import com.sn.framework.module.project.domain.Project;
import com.sn.framework.module.project.repo.IProjectRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * Created by zsl on 2018/7/12.
 */
@Repository
public class ProjectRepoImpl extends AbstractRepository<Project, String> implements IProjectRepo {

    private final Logger logger = LoggerFactory.getLogger(ProjectRepoImpl.class);


}
