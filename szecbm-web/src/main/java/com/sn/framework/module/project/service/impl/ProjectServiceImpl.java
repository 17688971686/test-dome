package com.sn.framework.module.project.service.impl;

import com.sn.framework.core.common.SessionUtil;
import com.sn.framework.core.common.Validate;
import com.sn.framework.core.service.impl.SServiceImpl;
import com.sn.framework.core.web.PageModelDto;
import com.sn.framework.module.project.domain.Project;
import com.sn.framework.module.project.domain.Project_;
import com.sn.framework.module.project.model.ProjectDto;
import com.sn.framework.module.project.repo.IProjectRepo;
import com.sn.framework.module.project.service.IProjectService;
import com.sn.framework.module.sys.domain.Organ;
import com.sn.framework.module.sys.domain.User;
import com.sn.framework.module.sys.repo.IOrganRepo;
import com.sn.framework.module.sys.service.IOrganService;
import com.sn.framework.odata.OdataFilter;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private IProjectService projectService;

    @Autowired
    IOrganRepo organRepo;

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

    @Override
    public PageModelDto<ProjectDto> getProjectForManage(OdataJPA odata,String status) {
        List<Organ> organList = organRepo.findByOrganByOrgMLeader(SessionUtil.getUsername());
        if(Validate.isList(organList)|| USER_KEY_ADMIN.equals(SessionUtil.getUsername())){
            odata.addFilter(Project_.status.getName(), OdataFilter.Operate.EQ, status);
        }else{
            organList = organRepo.findByOrganByOrganManage(SessionUtil.getUsername());
            if(Validate.isList(organList)){
                List<OdataFilter> filters = new ArrayList<>();
                odata.addFilter(Project_.status.getName(), OdataFilter.Operate.EQ, status);
                for(int i = 0; i < organList.size(); i++){
                    filters.add(new OdataFilter(Project_.mainOrgId.getName(), OdataFilter.Operate.EQ, organList.get(i).getOrganId()));
                }
                OdataFilter orFilter = new OdataFilter(null, OdataFilter.Operate.OR, filters);
                odata.addFilter(orFilter);
            }else {
                organList = organRepo.findByOrganByOrganLead(SessionUtil.getUsername());
                if(Validate.isList(organList)){
                    List<OdataFilter> filters = new ArrayList<>();
                    odata.addFilter(Project_.status.getName(), OdataFilter.Operate.EQ, status);
                    filters.add(new OdataFilter(Project_.mainOrgId.getName(), OdataFilter.Operate.EQ, organList.get(0).getOrganId()));
                    filters.add(new OdataFilter(Project_.assistOrgId.getName(), OdataFilter.Operate.LIKE, organList.get(0).getOrganId()));
                    OdataFilter orFilter = new OdataFilter(null, OdataFilter.Operate.OR, filters);
                    odata.addFilter(orFilter);

                }else{
                    List<OdataFilter> filters = new ArrayList<>();
                    odata.addFilter(Project_.status.getName(), OdataFilter.Operate.EQ, status);
                    filters.add(new OdataFilter(Project_.mainUser.getName(), OdataFilter.Operate.EQ,SessionUtil.getUserInfo().getUserId()));
                    filters.add(new OdataFilter(Project_.assistUser.getName(), OdataFilter.Operate.LIKE, SessionUtil.getUserInfo().getUserId()));
                    OdataFilter orFilter = new OdataFilter(null, OdataFilter.Operate.OR, filters);
                    odata.addFilter(orFilter);
                }
            }
        }
        return projectService.findPageByOdata(odata);
    }
}
