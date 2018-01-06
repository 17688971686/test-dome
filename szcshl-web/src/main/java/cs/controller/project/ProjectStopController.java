package cs.controller.project;

import cs.ahelper.IgnoreAnnotation;
import cs.common.Constant;
import cs.common.ResultMsg;
import cs.domain.project.ProjectStop;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.project.ProjectStopDto;
import cs.repository.odata.ODataObj;
import cs.service.project.ProjectStopService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping(name="项目暂停",path="projectStop")
@IgnoreAnnotation
public class ProjectStopController {
	
	private String ctrlName="projectStop";
	@Autowired
	private ProjectStopService projectStopService;

   @RequiresAuthentication
    //@RequiresPermissions("projectStop#findByOData#post")
   @RequestMapping(name="查询暂停项目",path="findByOData" ,method = RequestMethod.POST)
   @ResponseBody
   public PageModelDto<ProjectStopDto> getProjectStop(HttpServletRequest request) throws ParseException {
	   ODataObj odataObj = new ODataObj(request);
	   PageModelDto<ProjectStopDto> pageModelDto = projectStopService.findProjectStopByStopId(odataObj);
	   return pageModelDto;
   }

    @RequiresAuthentication
    //@RequiresPermissions("projectStop#initProjectBySignId#get")
	@RequestMapping(name="通过收文id获取收文信息",path="initProjectBySignId" ,method=RequestMethod.POST)
	@ResponseBody
	public SignDispaWork initProjectBySignId(@RequestParam  String signId){
		return projectStopService.findSignBySignId(signId);
	}

	@RequiresAuthentication
	//@RequiresPermissions("projectStop#initProjectBySignId#get")
	@RequestMapping(name="通过项目id获取暂停项目信息",path="getProjectStopBySignId" ,method=RequestMethod.POST)
	@ResponseBody
	public List<ProjectStop>  getProjectStopBySignId(@RequestParam  String signId){
		List<ProjectStop> projectStopList = projectStopService.findProjectStopBySign(signId);
		return projectStopList;
	}

   /* @RequiresAuthentication
    //@RequiresPermissions("projectStop#countUsedWorkday#get")
	@RequestMapping(name="计算已用工作日",path="countUsedWorkday",method = RequestMethod.GET)
	@ResponseBody
	public int countUsedWorkday(@RequestParam  String signId){
		return projectStopService.countUsedWorkday(signId);
	}*/

    @RequiresAuthentication
	@RequestMapping(name="发起流程", path="savePauseProject" ,method=RequestMethod.POST)
	@ResponseBody
	public ResultMsg pauseProject(@RequestBody  ProjectStopDto projectStopDto){
		return projectStopService.savePauseProject(projectStopDto);
	}

	@RequiresAuthentication
	@RequestMapping(name="保存信息", path="saveProjectStop" ,method=RequestMethod.POST)
	@ResponseBody
	public ResultMsg saveProjectStop(@RequestBody  ProjectStopDto projectStopDto){
		return projectStopService.saveProjectStop(projectStopDto);
	}


    @RequiresAuthentication
    //@RequiresPermissions("projectStop#getProjectStopByStopId#get")
	@RequestMapping(name="通过Id获取暂停项目信息", path="getProjectStopByStopId" ,method=RequestMethod.POST)
	@ResponseBody
	public ProjectStopDto getProjectStopByStopId(@RequestParam String stopId){
		return projectStopService.getProjectStopByStopId(stopId);
	}

    /*@RequiresAuthentication
    //@RequiresPermissions("projectStop#updateProjectStop#post")
	@RequestMapping(name="更新暂停项目审批意见",path="updateProjectStop" ,method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateProjectStop(@RequestBody ProjectStopDto projectStopDto){
		projectStopService.updateProjectStop(projectStopDto);
	}*/

    @RequiresAuthentication
	//@RequiresPermissions("projectStop#findPausingProject#get")
	@RequestMapping(name="判断该项目是否已申请暂停而未处理完",path="findPausingProject",method = RequestMethod.POST)
	@ResponseBody
	public String findPausingProject(@RequestParam  String signId){
		List<ProjectStop> projectStopList = projectStopService.findProjectStopBySign(signId);
		String result =null;
		for(ProjectStop p : projectStopList){
			if(Constant.EnumState.YES.getValue().equals(p.getIsactive()) && !Constant.EnumState.YES.getValue().equals(p.getIsOverTime())){
				result= "pausingProject";
				break;
			}
		}
		return result;
	}

	@RequiresAuthentication
	@RequestMapping(name="通过收文ID获取审批通过的项目信息" , path="getListInfo" , method = RequestMethod.POST)
	@ResponseBody
	public List<ProjectStopDto> getListInfo(@RequestParam String signId){
		return projectStopService.getStopList(signId);
	}


	@RequiresAuthentication
//	@RequiresPermissions("projectStop#html/projectStopInfo#get")
	@RequestMapping(name="项目暂停信息表单（多个）" , path = "html/projectStopInfo" , method = RequestMethod.GET)
	public String stopInfoList(){
		return ctrlName + "/projectStopInfo";
	}

	@RequiresAuthentication
//	@RequiresPermissions("projectStop#html/projectStopForm#get")
	@RequestMapping(name="项目暂停表单" , path="html/projectStopForm" , method =  RequestMethod.GET)
	public String projectForm(){
		return ctrlName + "/projectStopForm";
	}


	@RequiresAuthentication
//	@RequiresPermissions("projectStop#html/pauseProjectList#get")
	@RequestMapping(name="项目暂停审批"  , path="html/pauseProjectList")
	public String stopApprove(){
		return ctrlName +"/pauseProjectList";
	}
}
