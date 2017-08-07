package cs.controller.project;

import cs.common.Constant;
import cs.domain.project.ProjectStop;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.project.ProjectStopDto;
import cs.repository.odata.ODataObj;
import cs.service.project.ProjectStopService;
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
public class ProjectStopController {
	
	private String ctrlName="projectStop";
	@Autowired
	private ProjectStopService projectStopService;


   /* @RequiresPermissions("ProjectStop#createpStop#post")
    @RequestMapping(name="项目暂停",path="projectStop",method=RequestMethod.POST)
    @ResponseBody
    public void projectStop(@RequestParam String signid,String taskid){
    	projectStopService.addProjectStop(signid,taskid);
    	
    }
    
    @RequiresPermissions("ProjectStop#projectStart#post")
    @RequestMapping(name="项目启动",path="projectStart",method=RequestMethod.POST)
    @ResponseBody
    public void projectStart(@RequestParam String signid,String taskid){
    	projectStopService.projectStart(signid,taskid);
    }*/

   @RequiresPermissions("projectStop#findByOData#post")
   @RequestMapping(name="查询暂停项目",path="findByOData" ,method = RequestMethod.POST)
   @ResponseBody
   public PageModelDto<ProjectStopDto> getProjectStop(HttpServletRequest request) throws ParseException {
	   ODataObj odataObj = new ODataObj(request);
	   PageModelDto<ProjectStopDto> pageModelDto = projectStopService.findProjectStopByStopId(odataObj);
	   return pageModelDto;
   }


	@RequiresPermissions("projectStop#initProjectBySignId#get")
	@RequestMapping(name="通过收文id获取收文信息",path="initProjectBySignId" ,method=RequestMethod.GET)
	@ResponseBody
	public SignDispaWork initProjectBySignId(@RequestParam  String signId){
		return projectStopService.findSignBySignId(signId);
	}

	@RequiresPermissions("projectStop#countUsedWorkday#get")
	@RequestMapping(name="计算已用工作日",path="countUsedWorkday",method = RequestMethod.GET)
	@ResponseBody
	public int countUsedWorkday(@RequestParam  String signId){
		return projectStopService.countUsedWorkday(signId);
	}

	@RequiresPermissions("projectStop#savePauseProject#post")
	@RequestMapping(name="添加暂停项目", path="savePauseProject" ,method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void pauseProject(@RequestBody  ProjectStopDto projectStopDto){
		projectStopService.savePauseProject(projectStopDto);
	}

	@RequiresPermissions("projectStop#getProjectStopByStopId#get")
	@RequestMapping(name="通过Id获取暂停项目信息", path="getProjectStopByStopId" ,method=RequestMethod.GET)
	@ResponseBody
	public ProjectStopDto getProjectStopByStopId(@RequestParam String stopId){
		return projectStopService.getProjectStopByStopId(stopId);
	}

	@RequiresPermissions("projectStop#updateProjectStop#post")
	@RequestMapping(name="更新暂停项目审批意见",path="updateProjectStop" ,method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateProjectStop(@RequestBody ProjectStopDto projectStopDto){
		projectStopService.updateProjectStop(projectStopDto);
	}

	@RequiresPermissions("projectStop#findPausingProject#get")
	@RequestMapping(name="判断该项目是否已申请暂停而未处理完",path="findPausingProject",method = RequestMethod.GET)
	@ResponseBody
	public String findPausingProject(@RequestParam  String signId){
		List<ProjectStop> projectStopList = projectStopService.findProjectStopBySign(signId);
		String result =null;
		for(ProjectStop p : projectStopList){
			if(! Constant.EnumState.YES.getValue().equals(p.getApproveStatus())){
				result= "pausingProject";
				break;
			}
		}
		return result;
	}

	@RequestMapping(name="获取待办暂停项目个数",path= "pauseProjectCount" ,method=RequestMethod.GET)
    @ResponseBody
	public long getCount(HttpServletRequest request) throws ParseException{
		ODataObj oDataObj=new ODataObj(request);
		return projectStopService.findProjectStopByStopId(oDataObj).getCount();
	}
}
