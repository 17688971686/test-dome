package com.sn.framework.module.project.controller;

import com.sn.framework.core.common.SNKit;
import com.sn.framework.core.syslog.OperatorType;
import com.sn.framework.core.syslog.SysLog;
import com.sn.framework.core.util.CreateTemplateUtils;
import com.sn.framework.core.web.PageModelDto;
import com.sn.framework.jxls.JxlsUtils;
import com.sn.framework.module.project.domain.Project_;
import com.sn.framework.module.project.model.ProjectDto;
import com.sn.framework.module.project.service.IProjectService;
import com.sn.framework.module.sys.service.IDictService;
import com.sn.framework.module.sys.service.ISysFileService;
import com.sn.framework.odata.OdataFilter;
import com.sn.framework.odata.impl.jpa.OdataJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import static com.sn.framework.common.StringUtil.ISO_8859_1;
import static com.sn.framework.common.StringUtil.UTF_8;

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
    @Autowired
    private IDictService dictService;

    @Autowired
    private ISysFileService sysFileService;

    @Value("${sn.multipart.disk-path}")
    private String ATTACHMENT_DISK_PATH;


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

    @RequestMapping(name = "项目查看", path = "html/view", method = RequestMethod.GET)
    public String view() {
        return this.ctrlName + "/projectView";
    }

    @RequestMapping(name = "根据id获取项目信息", path = "findById", method = RequestMethod.GET)
    @ResponseBody
    public ProjectDto getProjectById(@RequestParam String id) {
        return projectService.getById(id);
    }

    @RequestMapping(name = "创建项目", path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @SysLog(businessType = "创建项目",operatorType = OperatorType.ADD,serviceclass = IProjectService.class,idName = "id")
    public void post(@RequestBody ProjectDto dto) {
        projectService.create(dto);
    }

    @RequestMapping(name = "更新项目", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @SysLog(businessType = "更新项目",operatorType = OperatorType.UPDATE,serviceclass = IProjectService.class,idName = "id")
    public void put(@RequestBody ProjectDto dto) {
        projectService.update(dto);
    }

   @RequestMapping(name = "项目恢复", path = "restore", method = RequestMethod.POST)
   @ResponseStatus(value = HttpStatus.NO_CONTENT)
   @SysLog(businessType = "项目恢复",operatorType = OperatorType.RESTORE,serviceclass = IProjectService.class,idName = "id")
    public void restore(@RequestBody ProjectDto dto) {
        projectService.update(dto);
    }

    @RequestMapping(name = "项目作废", path = "cancel", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @SysLog(businessType = "项目作废",operatorType = OperatorType.CANCEL,serviceclass = IProjectService.class,idName = "id")
    public void cancel(@RequestBody ProjectDto dto) {
        projectService.update(dto);
    }

    @RequestMapping(name = "删除项目", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @SysLog(businessType = "删除项目",operatorType = OperatorType.DELETE,serviceclass = IProjectService.class,idName = "id")
    public void delete(@RequestParam String id) {
        String[] ids = id.split(",");
        if (ids.length > 1) {
            projectService.deleteByIds(ids);
        } else {
            projectService.deleteById(id);
        }
    }

    @RequestMapping(name = "获取UUID", path = "createUUID", method = RequestMethod.GET)
    @ResponseBody
    public String createUUID() {
        return UUID.randomUUID().toString();
    }



    @GetMapping(name = "导出汇总打印", path = "exportPro2")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createSmallReport2(HttpServletResponse resp,OdataJPA odata,ProjectDto projectDto) throws IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        PageModelDto<ProjectDto> pageModelDto = projectService.findPageByOdata(odata);
        resultMap.put("projectList",pageModelDto.getValue());
        // 项目行业
        resultMap.put("TRANSACT_DEPARTMENT", dictService.findChildrenMapById("TRANSACT_DEPARTMENT"));
        resultMap.put("PRO_STAGE", dictService.findChildrenMapById("PRO_STAGE"));
        resultMap.put("title","项目信息列表");
        resp.setCharacterEncoding(UTF_8.name());
        resp.setContentType("application/vnd.ms-excel");
        String filename = new String("smallProject.xls".getBytes(UTF_8), ISO_8859_1);
        resp.setHeader("Content-disposition", "attachment;filename=" + filename);
        //TRANSACT_DEPARTMENT[myGroup.item.govInvestProject.projectIndustry].dictName

        JxlsUtils.exportExcel("classpath:jxls/smallProject.xls", resp.getOutputStream(), resultMap);
    }

    @GetMapping(name = "项目导出", path = "exportPro")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createSmallReport2(HttpServletResponse resp, ProjectDto projectDto) {
        List<ProjectDto> projectList = projectService.findAll();
        Map<String , Object> dataMap = new HashMap<>();
        dataMap.put("projectList",projectList);
        String templateName = "project/proInfoExport";
        String outFilePath = ATTACHMENT_DISK_PATH + File.separator + "proInf" +
                "oExport.doc";
        File exclFile = CreateTemplateUtils.createMonthTemplate(dataMap, templateName, outFilePath);
        resp.setCharacterEncoding(UTF_8.name());
        resp.setContentType("application/msword");
        String filename = new String("项目信息列表.doc".getBytes(UTF_8), ISO_8859_1);
        resp.setHeader("Content-disposition", "attachment;filename=" + filename);
        SNKit.fileDownload(resp, exclFile);
    }


}
