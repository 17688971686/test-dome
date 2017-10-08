package cs.controller.sys;

import com.alibaba.fastjson.JSON;
import cs.ahelper.MudoleAnnotation;
import cs.common.Constant;
import cs.common.utils.DateUtils;
import cs.common.utils.PropertyUtil;
import cs.common.utils.SessionUtil;
import cs.repository.repositoryImpl.flow.RuProcessTaskRepo;
import cs.repository.repositoryImpl.flow.RuTaskRepo;
import cs.service.project.ProjectStopService;
import cs.service.reviewProjectAppraise.AppraiseService;
import cs.service.rtx.RTXService;
import cs.service.sys.DictService;
import org.activiti.engine.task.TaskQuery;
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

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(name = "管理界面", path = "admin")
@MudoleAnnotation(name = "我的工作台",value = "permission#workbench")
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
    private ProjectStopService projectStopService;

    @Autowired
    private AppraiseService appraiseService;

    //@RequiresPermissions("admin#index#get")
    @RequiresAuthentication
    @RequestMapping(name = "首页", path = "index")
    public String index(Model model) {
        model.addAttribute("user", SessionUtil.getLoginName());
        model.addAttribute("DICT_ITEMS", JSON.toJSONString(dictService.getDictItemByCode(null)));
        /*String userState = rtxService.queryUserState(null,SessionUtil.getLoginName());
        if("0".equals(userState) || "2".equals(userState)){
            model.addAttribute("RTX_SEESION_KEY", rtxService.getSessionKey(null,SessionUtil.getLoginName()));
            PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
            model.addAttribute("RTX_IP", propertyUtil.readProperty("RTX_IP"));
        }*/
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

    //@RequiresPermissions("admin#myCountInfo#get")
    @RequiresAuthentication
    @RequestMapping(name = "个人待办信息查询", path = "myCountInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> tasksCount(HttpServletRequest request) throws ParseException {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("DO_SIGN_COUNT",ruProcessTaskRepo.findMyDoingTask());
        resultMap.put("DO_TASK_COUNT",ruTaskRepo.findMyDoingTask());

        if(SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())
                || SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())
                || SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.COMM_DEPT_DIRECTOR.getValue())
                || Constant.GENERALCONDUTOR.equals(SessionUtil.getDisplayName())){
            resultMap.put("PAUSE_COUNT",projectStopService.findMyPauseCount());
            resultMap.put("APPRAISE_COUNT" , appraiseService.countApprove());
        }

        return resultMap;
    }

    @RequiresPermissions("admin#gtasks#get")
    @RequestMapping(name = "待办事项", path = "gtasks")
    public String gtasks(Model model) {

        return ctrlName + "/gtasks";
    }

    @RequiresPermissions("admin#agendaTasks#get")
    @RequestMapping(name = "待办任务", path = "agendaTasks")
    public String agendaTasks(Model model) {

        return ctrlName + "/agendaTasks";
    }

    @RequiresPermissions("admin#doingTasks#get")
    @RequestMapping(name = "在任务", path = "doingTasks")
    public String doingTasks(Model model) {

        return ctrlName + "/doingTasks";
    }

    @RequiresPermissions("admin#dtasks#get")
    @RequestMapping(name = "在办项目", path = "dtasks")
    public String dtasks(Model model) {

        return ctrlName + "/dtasks";
    }

    @RequiresPermissions("admin#personDtasks#get")
    @RequestMapping(name = "个人在办项目", path = "personDtasks")
    public String personDtasks(Model model) {

        return ctrlName + "/personDtasks";
    }

    @RequiresPermissions("admin#etasks#get")
    @RequestMapping(name = "办结项目", path = "etasks")
    public String etasks(Model model) {

        return ctrlName + "/etasks";
    }

    @RequiresAuthentication
    //@RequiresPermissions("admin#edit#get")
    @RequestMapping(name = "拟补充资料函",  path = "edit")
    public String eidt(Model model) {
    	
    	return ctrlName + "/edit";
    }
}
