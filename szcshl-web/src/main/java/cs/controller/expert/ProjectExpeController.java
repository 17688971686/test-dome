package cs.controller.expert;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cs.common.ResultMsg;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import cs.model.expert.ProjectExpeDto;
import cs.repository.odata.ODataObj;
import cs.service.expert.ProjectExpeService;

@Controller
@RequestMapping(name = "项目经验", path = "projectExpe")
public class ProjectExpeController {
    private String ctrlName = "projectExpe";
    @Autowired
    private ProjectExpeService projectExpeService;

    @RequiresPermissions("projectExpe#findByOdata#post")
    @RequestMapping(name = "查找项目经验", path = "findByOdata", method = RequestMethod.POST)
    public @ResponseBody
    List<ProjectExpeDto> getProject(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        return projectExpeService.getProject(odataObj);
    }

    @RequiresPermissions("projectExpe#saveExpe#post")
    @RequestMapping(name = "添加项目经验", path = "saveExpe", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveProject(@RequestBody ProjectExpeDto project) {
        return projectExpeService.saveProject(project);
    }

    @RequiresPermissions("projectExpe#deleteProject#delete")
    @RequestMapping(name = "删除项目经验", path = "deleteProject", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProject(@RequestParam String ids) {
        projectExpeService.deleteProject(ids);
    }

    @RequiresPermissions("projectExpe#updateProject#put")
    @RequestMapping(name = "更新项目经验", path = "updateProject", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateProject(@RequestBody ProjectExpeDto projectExpeDto) {
        projectExpeService.updateProject(projectExpeDto);
    }
}
