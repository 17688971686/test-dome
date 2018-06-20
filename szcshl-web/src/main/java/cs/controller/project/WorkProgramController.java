package cs.controller.project;

import cs.ahelper.IgnoreAnnotation;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.domain.project.WorkProgram;
import cs.model.project.WorkProgramDto;
import cs.service.project.WorkProgramService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(name = "工作方案", path = "workprogram")
@IgnoreAnnotation
public class WorkProgramController {

    private String ctrlName = "workprogram";

    @Autowired
    private WorkProgramService workProgramService;

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#addWork#post")
    @RequestMapping(name = "保存工作方案", path = "addWork", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody WorkProgramDto workProgramDto, Boolean isNeedWorkProgram) {
        return workProgramService.save(workProgramDto, isNeedWorkProgram);
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#saveBaseInfo#post")
    @RequestMapping(name = "保存项目基本信息", path = "saveBaseInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveBaseInfo(@RequestBody WorkProgramDto workProgramDto) {
        return workProgramService.saveBaseInfo(workProgramDto);
    }
    @RequiresAuthentication
    //@RequiresPermissions("workprogram#initWorkProgram#post")
    @RequestMapping(name = "初始化工作方案", path = "initFlowWP", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> initFlowWP(@RequestParam(required = true) String signId,@RequestParam(required = true) String taskId) {
        return workProgramService.initWorkProgram(signId,taskId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#initWorkProgram#post")
    @RequestMapping(name = "初始化项目基本信息", path = "initBaseInfo", method = RequestMethod.POST)
    @ResponseBody
    public WorkProgramDto initBaseInfo(@RequestParam(required = true) String signId) {
        return workProgramService.initBaseInfo(signId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#html/workMaintainList#post")
    @RequestMapping(name = "查询工作方案", path = "html/workMaintainList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> workMaintainList(@RequestParam(required = true) String signId) {
        return workProgramService.workMaintainList(signId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#initWorkProgramById#post")
    @RequestMapping(name = "根据ID查找工作方案信息", path = "initWorkProgramById", method = RequestMethod.POST)
    public @ResponseBody WorkProgramDto initWorkProgramById(@RequestParam(required = true) String workId){
    	WorkProgramDto work = workProgramService.initWorkProgramById(workId);
    	return work;
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        workProgramService.delete(id);
    }

    @RequiresAuthentication
    @RequestMapping(name = "删除工作方案", path = "deleteBySignId", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg deleteBySignId(@RequestParam(required = true) String signId) {
        return workProgramService.deleteBySignId(signId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#findByPrincipalUser#get")
    @RequestMapping(name="通过项目负责人获取项目信息", path="findByPrincipalUser" ,method=RequestMethod.GET)
    @ResponseBody
    public WorkProgramDto findByPrincipalUser(@RequestParam  String signId){
        return workProgramService.findByPrincipalUser(signId);
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#createMeetingDoc#get")
    @RequestMapping(name = "生成会前准备材料", path = "createMeetingDoc", method = RequestMethod.GET)
    @ResponseBody
    public ResultMsg createMeetingDoc(@RequestParam(required = true) String signId) {
        return workProgramService.createMeetingDoc(signId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "专家评审会修改", path = "updateReviewType", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg updateReviewType(@RequestBody WorkProgramDto workProgramDto) {
        return workProgramService.updateReviewType(workProgramDto.getSignId(),workProgramDto.getId(),workProgramDto.getReviewType());
    }


    @RequiresAuthentication
    @RequestMapping(name = "更新工作方案专家评审费用", path = "updateWPExpertCost", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateWPExpertCost(@RequestParam String wpId) {
        workProgramService.initExpertCost(wpId);
    }


    @RequiresAuthentication
    //@RequiresPermissions("workprogram#html/edit#get")
    @RequestMapping(name = "工作方案编辑", path = "html/edit", method = RequestMethod.GET)
    public String edit() {

        return ctrlName + "/edit";
    }

    @RequiresAuthentication
    //@RequiresPermissions("workprogram#html/baseEdit#get")
    @RequestMapping(name = "项目基本信息", path = "html/baseEdit", method = RequestMethod.GET)
    public String baseEdit() {

        return ctrlName + "/baseEdit";
    }

}
