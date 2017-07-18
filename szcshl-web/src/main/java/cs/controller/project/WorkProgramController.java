package cs.controller.project;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.model.project.SignDto;
import cs.model.project.WorkProgramDto;
import cs.service.project.WorkProgramService;

@Controller
@RequestMapping(name = "工作方案", path = "workprogram")
public class WorkProgramController {

    private String ctrlName = "workprogram";

    @Autowired
    private WorkProgramService workProgramService;


    @RequiresPermissions("workprogram#addWork#post")
    @RequestMapping(name = "保存工作方案", path = "addWork", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody WorkProgramDto workProgramDto, Boolean isNeedWorkProgram) {
        return workProgramService.save(workProgramDto, isNeedWorkProgram);
    }

    @RequiresPermissions("workprogram#initWorkProgram#get")
    @RequestMapping(name = "初始化工作方案", path = "html/initWorkProgram", method = RequestMethod.GET)
    public @ResponseBody
    WorkProgramDto initWorkBySignId(@RequestParam(required = true) String signId, String workProgramId) {
        WorkProgramDto workDto = workProgramService.initWorkProgram(signId, workProgramId);
        return workDto;
    }
    
    @RequiresPermissions("workprogram#initWorkProgramById#get")
    @RequestMapping(name = "根据ID查找工作方案信息", path = "html/initWorkProgramById", method = RequestMethod.GET)
    public @ResponseBody WorkProgramDto initWorkProgramById(@RequestParam(required = true) String workId){
    	WorkProgramDto work = workProgramService.initWorkProgramById(workId);
    	return work;
    }

    @RequiresPermissions("workprogram#waitSeleWP#post")
    @RequestMapping(name = "待选合并评审工作方案", path = "waitSeleWP", method = RequestMethod.POST)
    public @ResponseBody
    List<WorkProgramDto> waitSeleWP(@RequestParam(required = true) String mainBussnessId) {
        List<WorkProgramDto> resultList = workProgramService.waitSeleWP(mainBussnessId);
        return resultList;
    }

    @RequiresPermissions("workprogram#getSeleWPByMainId#post")
    @RequestMapping(name = "获取已选合并评审工作方案", path = "getSeleWPByMainId", method = RequestMethod.POST)
    public @ResponseBody
    List<WorkProgramDto> getSeleWPByMainId(@RequestParam(required = true) String mainBussnessId) throws Exception {
        List<WorkProgramDto> resultList = workProgramService.getSeleWPByMainId(mainBussnessId);
        return resultList;
    }

    @RequiresPermissions("workprogram#mergeWork#post")
    @RequestMapping(name = "保存合并评审", path = "mergeWork", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void mergeWork(@RequestParam(required = true) String mainBusinessId, @RequestParam(required = true) String signId,
                             @RequestParam(required = true) String businessId,@RequestParam(required = true) String linkSignId) {
        workProgramService.mergeWork(mainBusinessId, signId,businessId,linkSignId);
    }

    @RequiresPermissions("workprogram#deleteMergeWork#post")
    @RequestMapping(name = "删除合并评审工作方案", path = "deleteMergeWork", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMergeWork(@RequestParam(required = true) String mainBusinessId,String businessId) {
        workProgramService.deleteMergeWork(mainBusinessId,businessId);
    }
    
    @RequiresPermissions("workprogram##delete")
    @RequestMapping(name = "删除记录", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        workProgramService.delete(id);
    }

    @RequestMapping(name = "删除工作方案", path = "deleteBySignId", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteBySignId(@RequestParam(required = true) String signId) {
        workProgramService.deleteBySignId(signId);
    }

    @RequiresPermissions("workprogram#createMeetingDoc#get")
    @RequestMapping(name = "生成会前准备材料", path = "createMeetingDoc", method = RequestMethod.GET)
    @ResponseBody
    public ResultMsg createMeetingDoc(@RequestParam(required = true) String signId, @RequestParam(required = true) String workprogramId) {
        return workProgramService.createMeetingDoc(signId, workprogramId);
    }

    @RequiresPermissions("workprogram#html/edit#get")
    @RequestMapping(name = "工作方案编辑", path = "html/edit", method = RequestMethod.GET)
    public String edit() {

        return ctrlName + "/edit";
    }

    @RequiresPermissions("workprogram#html/baseEdit#get")
    @RequestMapping(name = "项目基本信息", path = "html/baseEdit", method = RequestMethod.GET)
    public String baseEdit() {

        return ctrlName + "/baseEdit";
    }

}
