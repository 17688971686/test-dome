package cs.controller.sys;

import cs.ahelper.MudoleAnnotation;
import cs.common.ResultMsg;
import cs.common.constants.Constant;
import cs.common.utils.DateUtils;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.sys.WorkdayDto;
import cs.repository.odata.ODataObj;
import cs.service.project.SignService;
import cs.service.sys.WorkdayService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static cs.common.constants.SysConstants.DATE_COLON;
import static cs.common.constants.SysConstants.SEPARATE_COMMA;

@Controller
@RequestMapping(name = "工作日管理", path = "workday")
@MudoleAnnotation(name = "系统管理", value = "permission#system")
public class WorkdayController {

    private String urlName = "workday";

    @Autowired
    private WorkdayService workdayService;

    @Autowired
    private SignService signService;

    //@RequiresPermissions("workday#findByOdataObj#post")
    @RequiresAuthentication
    @RequestMapping(name = "获取工作日所有数据", path = "findByOdataObj", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<WorkdayDto> getWorkday(HttpServletRequest request) throws ParseException {

        ODataObj odataObj = new ODataObj(request);

        return workdayService.getWorkday(odataObj);
    }

    //@RequiresPermissions("workday#getWorkdayById#get")
    @RequiresAuthentication
    @RequestMapping(name = "通过id获取单个对象", path = "getWorkdayById", method = RequestMethod.GET)
    @ResponseBody
    public WorkdayDto getWorkdayById(@RequestParam String id) {

        return workdayService.getWorkdayById(id);
    }

    @RequiresAuthentication
    //@RequiresPermissions("workday#createWorkday#post")
    @RequestMapping(name = "新增工作日", path = "createWorkday", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg cresateWorkday(@RequestBody WorkdayDto workdayDto) {
        ResultMsg resultMsg = null;
        //获取日期值
        String monthValue = workdayDto.getMonth(),dateStr = workdayDto.getDatess(), month = "", year = "", isNodate = "";
        if (!Validate.isString(monthValue) || !Validate.isString(dateStr)) {
            resultMsg = ResultMsg.error("请先输入日期");
            return resultMsg;
        }
        String[] monthValues = monthValue.split(DATE_COLON);
        if (!Validate.isEmpty(monthValues)) {
            if (1 == monthValues.length) {
                month = monthValues[0];
                year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            } else {
                year = monthValues[0];
                month = monthValues[1];
            }
        }

        boolean isrepeat = false;
        //把中文字符串转换为英文字符串
        dateStr = dateStr.replace("，", SEPARATE_COMMA);
        //截取字符串
        List strs = StringUtil.getSplit(dateStr, SEPARATE_COMMA);
        if (strs.size() > 1) {
            for (int i = 0; i < strs.size(); i++) {
                String dates = year + DATE_COLON + month + DATE_COLON + strs.get(i);
                workdayDto.setDates(DateUtils.converToDate(dates, null));
                isrepeat = workdayService.isRepeat(workdayDto.getDates());
                if (isrepeat) {
                    if (isNodate != "") {
                        isNodate += SEPARATE_COMMA + strs.get(i) + "号";
                    } else {
                        isNodate += month + "月" + strs.get(i) + "号";
                    }
                } else {
                    resultMsg = workdayService.createWorkday(workdayDto);
                }
            }
        } else {
            String dates = year + DATE_COLON + month + DATE_COLON + dateStr;
            workdayDto.setDates(DateUtils.converToDate(dates, null));
            isrepeat = workdayService.isRepeat(workdayDto.getDates());
            if (isrepeat) {
                isNodate += month + "月" + dateStr + "号";
            } else {
                resultMsg = workdayService.createWorkday(workdayDto);
            }
        }
        //当有重复日期时，返回重复日期的提示
        if (Validate.isString(isNodate)) {
            resultMsg = ResultMsg.error(isNodate+"已存在，不能重复添加！");
        }

        return resultMsg;
    }

    //@RequiresPermissions("workday##put")
    @RequiresAuthentication
    @RequestMapping(name = "更新", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody WorkdayDto workdayDto) {
        workdayService.updateWorkday(workdayDto);
    }

    //@RequiresPermissions("workday##delete")
    @RequiresAuthentication
    @RequestMapping(name = "删除", path = "", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String id) {
        workdayService.deleteWorkday(id);
    }

    //begin html
    @RequiresPermissions("workday#html/list#get")
    @RequestMapping(name = "工作日管理", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return urlName + "/list";

    }

    //@RequiresPermissions("workday#html/edit#get")
    @RequiresAuthentication
    @RequestMapping(name = "工作日列编辑", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return urlName + "/edit";

    }

    //@RequiresPermissions("workday#countWorkday#get")
    @RequiresAuthentication
    @RequestMapping(name = "计算工作日", path = "countWorkday", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void countWorkday() throws Exception {
        /*SchedulerFactory schedulderFactory=new StdSchedulerFactory();
		Scheduler sched=schedulderFactory.getScheduler();
		String cls="cs.quartz.execute.SignCountWorkdayExecute";
		String jobName="工作日计算";
		String time="0/10 * * * * ?";
		QuartzManager.addJob(sched, jobName, Class.forName(cls), time);*/
    }
}
