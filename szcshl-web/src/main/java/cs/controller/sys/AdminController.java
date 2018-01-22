package cs.controller.sys;

import com.alibaba.fastjson.JSON;
import cs.ahelper.MudoleAnnotation;
import cs.common.Constant;
import cs.common.utils.*;
import cs.model.sys.SysConfigDto;
import cs.repository.repositoryImpl.flow.RuProcessTaskRepo;
import cs.repository.repositoryImpl.flow.RuTaskRepo;
import cs.service.flow.FlowService;
import cs.service.project.AddSuppLetterService;
import cs.service.project.ProjectStopService;
import cs.service.project.SignService;
import cs.service.reviewProjectAppraise.AppraiseService;
import cs.service.rtx.RTXService;
import cs.service.sys.AnnountmentService;
import cs.service.sys.DictService;
import org.activiti.engine.task.TaskQuery;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cs.model.sys.UserDto;
import cs.service.sys.UserService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import rtx.RTXSvrApi;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cs.common.Constant.FTP_IP;
import static cs.common.Constant.RevireStageKey.KEY_FTPIP;

@Controller
@RequestMapping(name = "管理界面", path = "admin")
@MudoleAnnotation(name = "我的工作台", value = "permission#workbench")
public class AdminController {
    private String ctrlName = "admin";
    private static Logger logger = Logger.getLogger(AdminController.class.getName());
    @Autowired
    private UserService userService;
    @Autowired
    private DictService dictService;
    @Autowired
    private RTXService rtxService;
    @Autowired
    private RuTaskRepo ruTaskRepo;
    @Autowired
    private RuProcessTaskRepo ruProcessTaskRepo;
    @Autowired
    private AnnountmentService annService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private SignService signService;

    //@RequiresPermissions("admin#index#get")
    @RequiresAuthentication
    @RequestMapping(name = "首页", path = "index")
    public String index(HttpServletRequest request, HttpServletRequest response, Model model) {
        model.addAttribute("user", SessionUtil.getLoginName());
        model.addAttribute("DICT_ITEMS", JSON.toJSONString(dictService.getDictItemByCode(null)));
        //登录时候提醒一次就行了
        if (SessionUtil.getSession().getAttribute(Constant.NOTICE_KEY) != null) {
            List<String> noticeList = (List<String>) SessionUtil.getSession().getAttribute(Constant.NOTICE_KEY);
            model.addAttribute("NOTICE_LIST", JSON.toJSONString(noticeList));
            SessionUtil.getSession().removeAttribute(Constant.NOTICE_KEY);
        }

        //如果使用腾讯通，则判断
       if(rtxService.rtxEnabled()){
           String agent = request.getHeader("User-Agent").toLowerCase();
           //如果是IE浏览器，则登录腾讯通
           if (Tools.getBrowserName(agent).contains("ie") && !Constant.SUPER_USER.equals(SessionUtil.getLoginName())) {
               String userState = rtxService.queryUserState(null, SessionUtil.getLoginName());
               if ("0".equals(userState) || "2".equals(userState) ) {
                   model.addAttribute("RTX_SEESION_KEY", rtxService.getSessionKey(null, SessionUtil.getLoginName()));
                   PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
                   model.addAttribute("RTX_IP", propertyUtil.readProperty("RTX_IP"));
                   String rtxName = Validate.isString(SessionUtil.getUserInfo().getRtxName())?SessionUtil.getUserInfo().getRtxName():SessionUtil.getLoginName();
                   model.addAttribute("RTX_NAME", rtxName);
               }
           }
       }
        return ctrlName + "/index";
    }

    //@RequiresPermissions("admin#welcome#get")
    @RequiresAuthentication
    @RequestMapping(name = "欢迎页", path = "welcome")
    public String welcome(Model model) {
        UserDto user = userService.findUserByName(SessionUtil.getLoginName());
        if (user != null) {
            model.addAttribute("user", user.getLoginName());
            model.addAttribute("lastLoginDate", DateUtils.toStringDay(user.getLastLoginDate()));
        }
        return ctrlName + "/welcome";
    }

    //@RequiresPermissions("admin#myCountInfo#get")是否
    @RequiresAuthentication
    @RequestMapping(name = "个人待办信息查询", path = "myCountInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> tasksCount(HttpServletRequest request) throws ParseException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("DO_SIGN_COUNT", ruProcessTaskRepo.findMyDoingTask());
        resultMap.put("DO_TASK_COUNT", ruTaskRepo.findMyDoingTask());
        //如果是项目签收人员，还要加上项目签收数量
        if(SessionUtil.checkPermissions("sign#html/list#get")){
            resultMap.put("GET_SIGN_COUNT", signService.findSignCount());
        }
        return resultMap;
    }

    @RequiresAuthentication
    @RequestMapping(name = "初始化个人首页", path = "initWelComePage", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> initWelComePage(HttpServletRequest request) throws ParseException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //根据不同的角色，初始化不同的页面（待实现）
        /*if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())){
            logger.info("副主任登录系统");
        }else if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue())){
            logger.info("主任登录系统");
        }*/
        /*******************   以下是普通用户的首页   ********************/
        //1、查询个人待办项目
        resultMap.put("proTaskList", flowService.queryMyRunProcessTasks(6));
        //2、查询个人待办任务
        resultMap.put("comTaskList", flowService.queryMyHomeAgendaTask());
        //3、查询通知公告
        resultMap.put("annountmentList", annService.getHomePageAnnountment());
        //4、查询办结任务
        resultMap.put("endTaskList", flowService.queryMyEndTasks());
        return resultMap;
    }


    @RequiresPermissions("admin#gtasks#get")
    @RequestMapping(name = "待办项目", path = "gtasks")
    public String gtasks(Model model) {

        return ctrlName + "/gtasks";
    }

    @RequiresPermissions("admin#dtasks#get")
    @RequestMapping(name = "在办项目", path = "dtasks")
    public String dtasks(Model model) {

        return ctrlName + "/dtasks";
    }


    @RequiresPermissions("admin#agendaTasks#get")
    @RequestMapping(name = "待办任务", path = "agendaTasks")
    public String agendaTasks(Model model) {

        return ctrlName + "/agendaTasks";
    }

    //    @RequiresAuthentication
    @RequiresPermissions("admin#doingTasks#get")
    @RequestMapping(name = "在办任务", path = "doingTasks")
    public String doingTasks(Model model) {

        return ctrlName + "/doingTasks";
    }

    @RequiresAuthentication
    //@RequiresPermissions("admin#edit#get")
    @RequestMapping(name = "拟补充资料函", path = "edit")
    public String eidt(Model model) {

        return ctrlName + "/edit";
    }
}
