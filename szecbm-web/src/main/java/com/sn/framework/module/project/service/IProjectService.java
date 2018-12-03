package com.sn.framework.module.project.service;

import com.sn.framework.core.service.ISService;
import com.sn.framework.core.web.PageModelDto;
import com.sn.framework.module.project.model.ProjectDto;
import com.sn.framework.odata.impl.jpa.OdataJPA;


/**
 * Created by Administrator on 2018-7-12.
 */
public interface IProjectService extends ISService<ProjectDto> {

    /**
     * 验证是否是项目负责人
     * @return
     */
    boolean checkCurUserIsPri(String projId);


    PageModelDto<ProjectDto> getProjectForManage(OdataJPA odata,String status);

}
