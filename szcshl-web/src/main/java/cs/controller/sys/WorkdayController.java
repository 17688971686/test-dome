package cs.controller.sys;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.common.utils.QuartzManager;
import cs.model.PageModelDto;
import cs.model.sys.WorkdayDto;
import cs.repository.odata.ODataObj;
import cs.service.project.SignService;
import cs.service.sys.WorkdayService;

@Controller
@RequestMapping(name="工作日管理",path="workday")
public class WorkdayController {

	private String urlName="workday";

	@Autowired
	private WorkdayService workdayService;
	
	@Autowired
	private SignService signService;

	@RequiresPermissions("workday#findByOdataObj#post")
	@RequestMapping(name="获取工作日所有数据",path="findByOdataObj",method=RequestMethod.POST)
	@ResponseBody
	public PageModelDto<WorkdayDto> getWorkday(HttpServletRequest request) throws ParseException{

		ODataObj odataObj=new ODataObj(request);

		return workdayService.getWorkday(odataObj);
	}

	@RequiresPermissions("workday#getWorkdayById#get")
	@RequestMapping(name="通过id获取单个对象",path="getWorkdayById",method=RequestMethod.GET)
	@ResponseBody
	public WorkdayDto getWorkdayById(@RequestParam String id){

		return workdayService.getWorkdayById(id);
	}

	@RequiresPermissions("workday#createWorkday#post")
	@RequestMapping(name="新增工作日",path="createWorkday",method=RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.CREATED)
	public void cresateWorkday(@RequestBody WorkdayDto workdayDto){
		workdayService.createWorkday(workdayDto);
	}

	@RequiresPermissions("workday##put")
	@RequestMapping(name="更新",path="",method=RequestMethod.PUT)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void update(@RequestBody WorkdayDto workdayDto){
		workdayService.updateWorkday(workdayDto);
	}

	@RequiresPermissions("workday##delete")
	@RequestMapping(name="删除",path="",method=RequestMethod.DELETE)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@RequestBody String id){
		workdayService.deleteWorkday(id);
	}

	//begin html
	@RequiresPermissions("workday#html/list#get")
	@RequestMapping(name="工作日列表",path="html/list",method=RequestMethod.GET)
	public String  list(){
		return urlName+"/list";

	}

	@RequiresPermissions("workday#html/edit#get")
	@RequestMapping(name="工作日列编辑",path="html/edit",method=RequestMethod.GET)
	public String  edit(){
		return urlName+"/edit";

	}

	@RequiresPermissions("workday#countWorkday#get")
	@RequestMapping(name="计算工作日",path="countWorkday",method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void countWorkday() throws Exception{
//		signService.selectSignNotFinish();
		SchedulerFactory schedulderFactory=new StdSchedulerFactory();
		Scheduler sched=schedulderFactory.getScheduler();
		String cls="cs.quartz.execute.SignCountWorkdayExecute";
		String jobName="工作日计算";
		String time="0/10 * * * * ?";
		QuartzManager.addJob(sched, jobName, Class.forName(cls), time);

	}
}
