package cs.controller.sys;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import cs.ahelper.MudoleAnnotation;
import cs.common.constants.Constant;
import cs.common.ResultMsg;
import cs.common.utils.QuartzManager;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.sys.Quartz;
import cs.domain.sys.Quartz_;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cs.model.PageModelDto;
import cs.model.sys.QuartzDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.QuartzRepo;
import cs.service.sys.QuartzService;

import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

/**
 * Description: 定时器配置 控制层
 * author: ldm
 * Date: 2017-6-20 10:47:42
 */
@Controller
@RequestMapping(name = "定时器配置", path = "quartz")
@MudoleAnnotation(name = "系统管理", value = "permission#system")
public class QuartzController {

    String ctrlName = "quartz";
    @Autowired
    private QuartzService quartzService;

    @Autowired
    private QuartzRepo quartzRepo;

    //@RequiresPermissions("quartz#findByOData#post")
    @RequiresAuthentication
    @RequestMapping(name = "获取数据", path = "findByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<QuartzDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<QuartzDto> quartzDtos = quartzService.get(odataObj);
        return quartzDtos;
    }

    //@RequiresPermissions("quartz##post")
    @RequiresAuthentication
    @RequestMapping(name = "创建记录", path = "", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg post(@RequestBody QuartzDto record) {
        if (!SUPER_ACCOUNT.equals(SessionUtil.getLoginName())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "你不是系统管理员，不能进行此操作！");
        }

        return quartzService.save(record);
    }

    @RequiresAuthentication
    @RequestMapping(name = "主键查询", path = "html/findById", method = RequestMethod.POST)
    public @ResponseBody
    QuartzDto findById(@RequestParam(required = true) String id) {
        return quartzService.findById(id);
    }

    //@RequiresPermissions("quartz##delete")
    @RequiresAuthentication
    @RequestMapping(name = "删除记录（设置为停用）", path = "", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg delete(@RequestParam String id) {
        if (!SUPER_ACCOUNT.equals(SessionUtil.getLoginName())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "你不是系统管理员，不能进行此操作！");
        }
        return quartzService.delete(id);
    }


    //@RequiresPermissions("quartz#quartzExecute#put")
    @RequiresAuthentication
    @RequestMapping(name = "执行定时器", path = "quartzExecute", method = RequestMethod.PUT)
    @ResponseBody
    public ResultMsg quartzExecute(@RequestParam String quartzId) {
        if (!SUPER_ACCOUNT.equals(SessionUtil.getLoginName())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "你不是系统管理员，不能进行此操作！");
        }
        return quartzService.quartzExecute(quartzId);
    }

    /**
     * 立刻执行一次
     *
     * @param quartzId
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(name = "立刻执行一次", path = "runOne", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg runOne(@RequestParam String quartzId) {
        ResultMsg resultMsg = new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"");
        Quartz quartz = quartzRepo.findById(Quartz_.id.getName(), quartzId);
        if (quartz == null) {
            resultMsg.setReMsg("操作失败，该任务已被删除！");
        }else{
            try {
                SchedulerFactory schedulderFactory = new StdSchedulerFactory();
                Scheduler sched = schedulderFactory.getScheduler();
                QuartzManager.runOnce(sched,quartz.getQuartzName());

                resultMsg.setFlag(true);
                resultMsg.setReCode(Constant.MsgCode.OK.getValue());
                resultMsg.setReMsg("执行成功！");
            }catch (Exception e){
                e.printStackTrace();
                resultMsg.setReMsg("执行任务异常");
            }
        }
        return  resultMsg;


    }

    //@RequiresPermissions("quartz#quartzStop#put")
    @RequiresAuthentication
    @RequestMapping(name = "停止定时器", path = "quartzStop", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg quartzStop(@RequestParam String quartzId) {
        if (!SUPER_ACCOUNT.equals(SessionUtil.getLoginName())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "你不是系统管理员，不能进行此操作！");
        }
        return quartzService.quartzStop(quartzId);
    }


    // begin#html
    @RequiresPermissions("quartz#html/list#get")
    @RequestMapping(name = "定时器管理", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    //@RequiresPermissions("quartz#html/edit#get")
    @RequiresAuthentication
    @RequestMapping(name = "编辑页面", path = "html/edit", method = RequestMethod.GET)
    public String edit() {
        return ctrlName + "/edit";
    }
    // end#html

}