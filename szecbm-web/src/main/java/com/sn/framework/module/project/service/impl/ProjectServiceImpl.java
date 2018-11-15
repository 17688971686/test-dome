package com.sn.framework.module.project.service.impl;

import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.core.common.Validate;
import com.sn.framework.core.service.impl.SServiceImpl;
import com.sn.framework.module.project.domain.Project;
import com.sn.framework.module.project.model.ProjectDto;
import com.sn.framework.module.project.repo.IProjectRepo;
import com.sn.framework.module.project.service.IProjectService;
import com.sn.framework.module.sys.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.sn.framework.core.Constants.USER_KEY_ADMIN;


/**
 * 用户信息  业务实现类
 *
 * @author zsl
 * @date 2018/7/19
 */
@Service
public class ProjectServiceImpl extends SServiceImpl<IProjectRepo,Project,ProjectDto> implements IProjectService {

    private final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Override
    public boolean checkCurUserIsPri(String projId) {
        User curUser = SessionUtil.getUserInfo();
        if(USER_KEY_ADMIN.equals(curUser.getUsername())){
            return  true;
        }
        String userId = curUser.getUserId();
        Project project = baseRepo.getById(projId);
        if(Validate.isObject(project) && (userId.equals(project.getMainUser()) || (Validate.isString(project.getAssistUser()) && project.getAssistUser().contains(userId)))){
            return true;
        }
        return false;
    }
}
