package cs.controller.sys;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cs.ahelper.MudoleAnnotation;
import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.DateUtils;
import cs.common.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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

import cs.model.PageModelDto;
import cs.model.sys.WorkdayDto;
import cs.repository.odata.ODataObj;
import cs.service.project.SignService;
import cs.service.sys.WorkdayService;

@Controller
@RequestMapping(name="工作日管理",path="workday")
@MudoleAnnotation(name = "系统管理",value = "permission#system")
public class WorkdayController {

	private String urlName="workday";

	@Autowired
	private WorkdayService workdayService;
	
	@Autowired
	private SignService signService;

	//@RequiresPermissions("workday#findByOdataObj#post")
    @RequiresAuthentication
    @RequestMapping(name="获取工作日所有数据",path="findByOdataObj",method=RequestMethod.POST)
	@ResponseBody
	public PageModelDto<WorkdayDto> getWorkday(HttpServletRequest request) throws ParseException{

		ODataObj odataObj=new ODataObj(request);

		return workdayService.getWorkday(odataObj);
	}

	//@RequiresPermissions("workday#getWorkdayById#get")
    @RequiresAuthentication
    @RequestMapping(name="通过id获取单个对象",path="getWorkdayById",method=RequestMethod.GET)
	@ResponseBody
	public WorkdayDto getWorkdayById(@RequestParam String id){

		return workdayService.getWorkdayById(id);
	}

    @RequiresAuthentication
	//@RequiresPermissions("workday#createWorkday#post")
	@RequestMapping(name="新增工作日",path="createWorkday",method=RequestMethod.POST)
	@ResponseBody
	public ResultMsg cresateWorkday(@RequestBody WorkdayDto workdayDto){
		ResultMsg resultMsg = null;
		String isNodate="";//存重复的日期
		boolean isrepeat;//判断是否已存在的日期
		String str=workdayDto.getDatess();
		//把中文字符串转换为英文字符串
		str = str.replace("，",",");
        //截取字符串
		List strs = StringUtil.getSplit(str,",");
		//获取当前年份
		Calendar now = Calendar.getInstance();
		String year= Integer.toString( now.get(Calendar.YEAR)) ;
		//获取月份
		String month=workdayDto.getMonth();
		if (strs.size() > 1) {
			for (int i=0;i<strs.size();i++){
              String dates=year+"-"+month+"-"+strs.get(i);
			  workdayDto.setDates(DateUtils.converToDate(dates,null));
				isrepeat=workdayService.isRepeat(workdayDto.getDates());
				if(isrepeat){
					if(isNodate!=""){
						isNodate+="、"+strs.get(i)+"号";
					}else{
						isNodate+=month+"月"+strs.get(i)+"号";
					}

				}else{
					resultMsg=  workdayService.createWorkday(workdayDto);
				}

			}
		} else {
			String dates=year+"-"+month+"-"+str;
			workdayDto.setDates(DateUtils.converToDate(dates,null));
			isrepeat=workdayService.isRepeat(workdayDto.getDates());
			if(isrepeat){
					isNodate+=month+"月"+str+"号";
			}else{
				resultMsg=  workdayService.createWorkday(workdayDto);
			}
		}
		if(!"".equals(isNodate)){//当有重复日期时，返回重复日期的提示
			if(resultMsg==null){
				resultMsg=new ResultMsg(false, Constant.MsgCode.OK.getValue(),isNodate);
			}else{
				resultMsg.setFlag(false);
				resultMsg.setReMsg(isNodate);
			}
		}

     return  resultMsg;
	}

	//@RequiresPermissions("workday##put")
    @RequiresAuthentication
    @RequestMapping(name="更新",path="",method=RequestMethod.PUT)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void update(@RequestBody WorkdayDto workdayDto){
		workdayService.updateWorkday(workdayDto);
	}

	//@RequiresPermissions("workday##delete")
    @RequiresAuthentication
    @RequestMapping(name="删除",path="",method=RequestMethod.DELETE)
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@RequestBody String id){
		workdayService.deleteWorkday(id);
	}

	//begin html
	@RequiresPermissions("workday#html/list#get")
	@RequestMapping(name="工作日管理",path="html/list",method=RequestMethod.GET)
	public String  list(){
		return urlName+"/list";

	}

	//@RequiresPermissions("workday#html/edit#get")
    @RequiresAuthentication
    @RequestMapping(name="工作日列编辑",path="html/edit",method=RequestMethod.GET)
	public String  edit(){
		return urlName+"/edit";

	}

	//@RequiresPermissions("workday#countWorkday#get")
    @RequiresAuthentication
    @RequestMapping(name="计算工作日",path="countWorkday",method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void countWorkday() throws Exception{
		/*SchedulerFactory schedulderFactory=new StdSchedulerFactory();
		Scheduler sched=schedulderFactory.getScheduler();
		String cls="cs.quartz.execute.SignCountWorkdayExecute";
		String jobName="工作日计算";
		String time="0/10 * * * * ?";
		QuartzManager.addJob(sched, jobName, Class.forName(cls), time);*/
	}
}
