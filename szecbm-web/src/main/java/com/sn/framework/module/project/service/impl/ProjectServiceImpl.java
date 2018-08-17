package com.sn.framework.module.project.service.impl;

import com.sn.framework.core.service.impl.SServiceImpl;
import com.sn.framework.module.project.domain.Project;
import com.sn.framework.module.project.model.ProjectDto;
import com.sn.framework.module.project.repo.IProjectRepo;
import com.sn.framework.module.project.service.IProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 用户信息  业务实现类
 *
 * @author zsl
 * @date 2018/7/19
 */
@Service
public class ProjectServiceImpl extends SServiceImpl<IProjectRepo,Project,ProjectDto> implements IProjectService {

    private final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    



}
