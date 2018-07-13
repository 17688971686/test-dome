package com.sn.framework.module.project.controller;

import com.sn.framework.core.web.PageModelDto;
import com.sn.framework.module.project.domain.Project_;
import com.sn.framework.module.project.model.ProjectDto;
import com.sn.framework.module.project.service.IProjectService;
import com.sn.framework.odata.OdataFilter;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 用户Controller
 *
 * @author zsl
 * @date 2018/7/12
 */
@Controller
@RequestMapping(name = "项目管理", path = "project")
public class ProjectController {
    private String ctrlName = "project";
    @Autowired
    private IProjectService projectService;


    @RequestMapping(name = "项目管理列表页面", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return this.ctrlName + "/projectList";
    }

    @RequestMapping(name = "项目管理列表页面", path = "html/cancel", method = RequestMethod.GET)
    public String cancelList() {
        return this.ctrlName + "/projectCancelList";
    }


    @RequestMapping(name = "获取项目信息", path = "proInfo", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<ProjectDto> getProjectForManage(OdataJPA odata) {
        odata.addFilter(Project_.status.getName(), OdataFilter.Operate.EQ, "1");
        return projectService.findPageByOdata(odata);
    }

    @RequestMapping(name = "获取作废项目信息", path = "cancelInfo", method = RequestMethod.GET)
    @ResponseBody
    public PageModelDto<ProjectDto> getCancelProjectForManage(OdataJPA odata) {
        odata.addFilter(Project_.status.getName(), OdataFilter.Operate.EQ, "2");
        PageModelDto<ProjectDto> pageModelDto = projectService.findPageByOdata(odata);
        return projectService.findPageByOdata(odata);
    }

    @RequestMapping(name = "项目编辑", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return this.ctrlName + "/projectEdit";
    }

    @RequestMapping(name = "根据id获取项目信息", path = "findById", method = RequestMethod.GET)
    @ResponseBody
    public ProjectDto getProjectById(@RequestParam String id) {
        return projectService.getById(id);
    }

    @RequestMapping(name = "创建项目", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void post(@RequestBody ProjectDto dto) {
        projectService.create(dto);
    }

    @RequestMapping(name = "更新项目", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void put(@RequestBody ProjectDto dto) {
        projectService.update(dto);
    }

    @RequestMapping(name = "删除项目", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestParam String id) {
        String[] ids = id.split(",");
        if (ids.length > 1) {
            projectService.deleteByIds(ids);
        } else {
            projectService.deleteById(id);
        }
    }

}
