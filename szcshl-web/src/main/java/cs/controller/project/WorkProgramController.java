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
    @RequestMapping(name = "工作方案提交", path = "addWork", method = RequestMethod.POST)
    @ResponseBody
    public WorkProgramDto post(@RequestBody WorkProgramDto workProgramDto, Boolean isNeedWorkProgram) throws Exception {
        workProgramService.save(workProgramDto, isNeedWorkProgram);
        return workProgramDto;
    }

    @RequiresPermissions("workprogram#initWorkBySignId#get")
    @RequestMapping(name = "工作方案编辑", path = "html/initWorkBySignId", method = RequestMethod.GET)
    public @ResponseBody
    WorkProgramDto initWorkBySignId(@RequestParam(required = true) String signId, String isMain) {
        WorkProgramDto workDto = workProgramService.initWorkBySignId(signId, isMain);
        return workDto;
    }
    
    @RequiresPermissions("workprogram#initWorkProgramById#get")
    @RequestMapping(name = "根据ID查找工作方案信息", path = "html/initWorkProgramById", method = RequestMethod.GET)
    public @ResponseBody WorkProgramDto initWorkProgramById(@RequestParam(required = true) String workId){
    	WorkProgramDto work = workProgramService.initWorkProgramById(workId);
    	return work;
    }

    @RequiresPermissions("workprogram#waitProjects#post")
    @RequestMapping(name = "待选项目列表", path = "waitProjects", method = RequestMethod.POST)
    public @ResponseBody
    List<SignDto> waitProjects(@RequestBody SignDto signDto) {
        List<SignDto> sign = workProgramService.waitProjects(signDto);
        return sign;
    }

    @RequiresPermissions("workprogram#selectedProject#post")
    @RequestMapping(name = "已选项目列表", path = "selectedProject", method = RequestMethod.POST)
    public @ResponseBody
    List<SignDto> selectedProject(@RequestParam String linkSignIds) {
        String[] ids = linkSignIds.split(",");
        List<SignDto> signList = workProgramService.selectedProject(ids);
        return signList;
    }

    @RequiresPermissions("workprogram#getInitSeleSignBysId#get")
    @RequestMapping(name = "初始化已选项目列表", path = "getInitSeleSignBysId", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> getInitSeleSignBysId(@RequestParam(required = true) String bussnessId) throws Exception {
        Map<String, Object> map = workProgramService.getInitSeleSignByIds(bussnessId);
        return map;
    }

    @RequiresPermissions("workprogram#getInitRelateData#post")
    @RequestMapping(name = "初始化页面获取关联数据", path = "getInitRelateData", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> getInitRelateData(@RequestParam(required = true) String signid) {
        Map<String, Object> map = workProgramService.getInitRelateData(signid);
        return map;
    }

    @RequiresPermissions("workprogram#mergeAddWork#get")
    @RequestMapping(name = "保存合并评审", path = "mergeAddWork", method = RequestMethod.GET)
    @ResponseBody
    public void mergeAddWork(@RequestParam(required = true) String signId, String linkSignId) {

        workProgramService.mergeAddWork(signId, linkSignId);
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
