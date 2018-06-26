package cs.controller.sys;

import com.alibaba.fastjson.JSON;
import cs.ahelper.LogMsg;
import cs.ahelper.MudoleAnnotation;
import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.utils.*;
import cs.domain.flow.RuProcessTask;
import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.model.sys.UserDto;
import cs.repository.repositoryImpl.flow.RuProcessTaskRepo;
import cs.repository.repositoryImpl.flow.RuTaskRepo;
import cs.repository.repositoryImpl.project.SignPrincipalRepo;
import cs.service.flow.FlowService;
import cs.service.project.SignPrincipalService;
import cs.service.project.SignService;
import cs.service.project.WorkProgramService;
import cs.service.rtx.RTXService;
import cs.service.sys.AnnountmentService;
import cs.service.sys.DictService;
import cs.service.sys.OrgDeptService;
import cs.service.sys.UserService;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static cs.common.constants.FlowConstant.*;
import static cs.common.constants.SysConstants.SUPER_ACCOUNT;

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
    @Autowired
    private OrgDeptService orgDeptService;
    @Autowired
    private SignPrincipalService signPrincipalService;
    @Autowired
    private WorkProgramService workProgramService;
    @Autowired
    private SignPrincipalRepo signPrincipalRepo;

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
        if (rtxService.rtxEnabled()) {
            String agent = request.getHeader("User-Agent").toLowerCase();
            //如果是IE浏览器，则登录腾讯通
            if (Tools.getBrowserName(agent).contains("ie") && !SUPER_ACCOUNT.equals(SessionUtil.getLoginName())) {
                String userState = rtxService.queryUserState(null, SessionUtil.getLoginName());
                if ("0".equals(userState) || "2".equals(userState)) {
                    model.addAttribute("RTX_SEESION_KEY", rtxService.getSessionKey(null, SessionUtil.getLoginName()));
                    PropertyUtil propertyUtil = new PropertyUtil(Constant.businessPropertiesName);
                    model.addAttribute("RTX_IP", propertyUtil.readProperty("RTX_IP"));
                    String rtxName = Validate.isString(SessionUtil.getUserInfo().getRtxName()) ? SessionUtil.getUserInfo().getRtxName() : SessionUtil.getLoginName();
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
        if (SessionUtil.checkPermissions("sign#html/list#get")) {
            resultMap.put("GET_SIGN_COUNT", signService.findSignCount());
        }
        if (SessionUtil.checkPermissions("sign#html/reserveList#get")) {
            resultMap.put("GET_RESERVESIGN_COUNT", signService.findReservesSignCount());
        }
        return resultMap;
    }


    @RequiresAuthentication
    @RequestMapping(name = "初始化个人首页", path = "getHomeInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getHomeInfo(HttpServletRequest request) throws ParseException, IOException, ClassNotFoundException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        boolean isSuper = SUPER_ACCOUNT.equals(SessionUtil.getLoginName()) ? true : false;
        //admin用户不用查询待办信息
        if (!isSuper) {
            //2、查询个人待办任务
            resultMap.put("comTaskList", flowService.queryMyHomeAgendaTask());
        }
        /*******************   以下是普通用户的首页   ********************/
        //3、查询通知公告
        resultMap.put("annountmentList", annService.getHomePageAnnountment());
        //4、查询办结任务
        //resultMap.put("endTaskList", flowService.queryMyEndTasks());
        return resultMap;
    }
    @RequiresAuthentication
    @RequestMapping(name = "获取首页项目统计信息", path = "getHomeProjInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getHomeProjInfo(HttpServletRequest request) throws ParseException, IOException, ClassNotFoundException {
        boolean isSuper = SUPER_ACCOUNT.equals(SessionUtil.getLoginName()) ? true : false;
        String curUserId = SessionUtil.getUserId();
        String LINE_SIGN_LIST_FLAG = "lineSign";
        String ISDISPLAY = "isdisplay";
        String PROTASKLIST = "proTaskList";

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> authMap = userService.getUserSignAuth();
        Integer authFlag = new Integer(authMap.get("leaderFlag").toString());
        List<String> orgIdList = (List<String>) authMap.get("orgIdList");
        List<RuProcessTask> authRuSignTask = flowService.queryRunProcessTasks(null, false, authFlag, orgIdList);
        if (Validate.isList(authRuSignTask)) {
            int totalCount = authRuSignTask.size();
            //1、过滤出当前用户待办的项目
            if (!isSuper) {
                int filterCount = 0;
                List<RuProcessTask> userProcessList = new ArrayList<>();
                for (int i = 0; i < totalCount; i++) {
                    RuProcessTask rt = authRuSignTask.get(i);
                    //过滤掉合并项目的次项目
                    if (rt.getReviewType() == null || "".equals(rt.getReviewType()) || Constant.EnumState.YES.getValue().equals(rt.getReviewType())
                            || (Constant.EnumState.NO.getValue().equals(rt.getReviewType())
                            && !FlowConstant.FLOW_SIGN_BMLD_SPW1.equals(rt.getNodeDefineKey()) && !FlowConstant.FLOW_SIGN_FGLD_SPW1.equals(rt.getNodeDefineKey()))
                            ) {
                        if (curUserId.equals(rt.getAssignee())
                                || (rt.getAssigneeList() != null && rt.getAssigneeList().indexOf(curUserId) > -1)) {

                            RuProcessTask newrt = new RuProcessTask();
                            BeanCopierUtils.copyProperties(rt, newrt);
                            userProcessList.add(newrt);
                            filterCount++;
                        }
                    }

                    if (filterCount == 6) {
                        break;
                    }
                }
                resultMap.put(PROTASKLIST, userProcessList);
            }

            /**
             * 分组统计柱状图和线形图数据
             1：按照部门（主任、副主任） 和人员统计（部长、信息化组）柱状图显示：
             1-1：部门 包括信息化组可以查看  该部门/组人员 的项目办理数量（横轴：部门人员、纵轴：项目在办项目数量）
             1-2：主任、副主任 可以查看自己管辖的各个部门的项目办理数量  （横轴：部门、纵轴：项目：项目在办数量）
             2：按照剩余工作日间查看项目的办理情况（横轴：项目签收日期、纵轴：剩余工作日）折线图展示：
             2-1：横轴坐标可以不显示项目签收日期，鼠标放到节点上需要显示项目名称，以及该项目的评审天数
             2-2：时间纵轴最多显示15天，负数到-3天
             2-3：鼠标点击节点进去后连接到该项目的详细办理情况
             */
            if (authFlag > 0) {
                resultMap.put(ISDISPLAY, false);
                //先按剩余工作日排序
                Collections.sort(authRuSignTask, new Comparator<RuProcessTask>() {
                    @Override
                    public int compare(RuProcessTask r1, RuProcessTask r2) {
                        Float r1Day = r1.getSurplusDays() == null ? 0f : r1.getSurplusDays();
                        Float r2Day = r2.getSurplusDays() == null ? 0f : r2.getSurplusDays();
                        if (r1Day == r2Day) {
                            return 0;
                        } else if (r1Day > r2Day) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
                List<String> existList = new ArrayList<>();
                //预签收项目列表
                List<RuProcessTask> preRuTask = authRuSignTask.stream().filter(x -> (!Validate.isObject(x.getSignDate()))).collect(Collectors.toList());
                if (Validate.isList(preRuTask)) {
                    Map<String, Map<String, Object>> preHistogramMap = getCountMap(authFlag, resultMap, preRuTask, authMap, orgIdList, existList);
                    if (!preHistogramMap.isEmpty()) {
                        resultMap.put("preHistogram", preHistogramMap);
                        existList = new ArrayList<>();
                    }
                }
                //过滤掉已发文的项目
                authRuSignTask = authRuSignTask.stream().filter(x -> (x.getSignDate() != null && x.getSignprocessState() < Constant.SignProcessState.END_DIS_NUM.getValue())).collect(Collectors.toList());
                int totalLength = authRuSignTask.size();
                //线性图
                List<RuProcessTask> lineList = new ArrayList<>();
                for (int i = 0; i < totalLength; i++) {
                    RuProcessTask rpt = authRuSignTask.get(i);
                    if (existList.contains(rpt.getBusinessKey())) {
                        continue;
                    } else {
                        existList.add(rpt.getBusinessKey());
                        lineList.add(rpt);
                    }
                }
                resultMap.put(LINE_SIGN_LIST_FLAG, lineList);
                //柱状图
                existList = new ArrayList<>();
                Map<String, Map<String, Object>> histogramMap = getCountMap(authFlag, resultMap, authRuSignTask, authMap, orgIdList, existList);
                resultMap.put("histogram", histogramMap);

                //(在办项目 ， 发文超期 ， 暂停 ， 少于3个工作日)
                int doingNum = 0 , dipathOverNum = 0  , stopNum = 0 , weekNum = 0 ;
                Map<String,Object> countTaskMap = histogramMap.get("COUNT_TASK_MAP");
                if(Validate.isMap(countTaskMap)){
                    for (Map.Entry<String, Object> entry : countTaskMap.entrySet()) {
                        RuProcessTask countRT = (RuProcessTask) entry.getValue();
                        doingNum ++ ;
                        switch (countRT.getLightState()){
                            case "6":
                                dipathOverNum ++ ;
                                break;
                            case "4" :
                                stopNum ++ ;
                                break;
                            case "5" :
                                weekNum ++;
                                break;
                            default: break;
                        }
                    }
                    resultMap.put("DOINGNUM" ,doingNum );
                    resultMap.put("DISPATHOVERNUM" , dipathOverNum);
                    resultMap.put("STOPNUM" , stopNum);
                    resultMap.put("WEEKNUM" , weekNum);
                }
                //把统计的去掉
                histogramMap.remove("COUNT_TASK_MAP");
            } else {
                resultMap.put(ISDISPLAY, true);
            }
        } else {
            resultMap.put(PROTASKLIST, null);
            resultMap.put(ISDISPLAY, true);
        }
        return resultMap;
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取首页会议和调研统计信息", path = "getHomeMeetInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getHomeMeetInfo(HttpServletRequest request) throws ParseException, IOException, ClassNotFoundException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("proMeetInfo",workProgramService.findProMeetInfo());
        return resultMap;
    }

    private Map<String, Map<String, Object>> getCountMap(int authFlag, Map<String, Object> resultMap, List<RuProcessTask> authRuSignTask, Map<String, Object> authMap, List<String> orgIdList, List<String> existList) {
        Map<String, Map<String, Object>> dataMap = new HashMap();
        String unWorkId = UUID.randomUUID().toString();
        //1、主任
        if (authFlag == 1) {
            for (RuProcessTask rpt : authRuSignTask) {
                boolean haveOrg = false;
                //主办部门
                if (Validate.isString(rpt.getmOrgId())) {
                    haveOrg = true;
                    setMapValue(dataMap, rpt.getmOrgId(), rpt, existList, rpt.getmOrgId());
                }
                if (Validate.isString(rpt.getaOrgId())) {
                    haveOrg = true;
                    List<String> aOrgIdList = StringUtil.getSplit(rpt.getaOrgId(), ",");
                    for (String orgId : aOrgIdList) {
                        setMapValue(dataMap, orgId, rpt, existList, orgId);
                    }
                }
                //没有分办的项目
                if (!haveOrg) {
                    setMapValue(dataMap, unWorkId, rpt, existList, null);
                }
            }
            if (Validate.isObject(resultMap)) {
                resultMap.put("XTYPE", "ORG");
            }
            //2、分管领导
        } else if (authFlag == 2) {
            for (RuProcessTask rpt : authRuSignTask) {
                if (Validate.isList(orgIdList)) {
                    for (String orgId : orgIdList) {
                        if (orgId.equals(rpt.getmOrgId()) || (rpt.getaOrgId() != null && rpt.getaOrgId().indexOf(orgId) > -1)) {
                            setMapValue(dataMap, orgId, rpt, existList, orgId);
                        }
                    }
                }
            }
            resultMap.put("XTYPE", "ORG");
            //3、部长
        } else if (authFlag == 3) {
            String userId = SessionUtil.getUserId();
            String orgId = orgIdList.get(0);
            //如果是部长，主办协办都要显示
            for(RuProcessTask rt : authRuSignTask){
                //如果还没分办，则要显示自己
                List<User> userList = signPrincipalRepo.getPrinUserList(rt.getBusinessKey(), orgId);
                if(Validate.isList(userList)){
                    boolean isMainUser = false;
                    String mainUserId = rt.getMainUserId();
                    if(Validate.isString(mainUserId)){
                        for(User user : userList){
                            if(mainUserId.equals(user.getId())){
                                isMainUser = true;
                                break;
                            }
                        }
                    }
                    if(isMainUser){
                        setMapValue(dataMap, mainUserId, rt, existList, orgId);
                    }else{
                        setMapValue(dataMap, "部门协办", rt, existList, orgId);
                    }
                }else{
                    if(FLOW_SIGN_BMFB1.equals(rt.getNodeDefineKey()) ||FLOW_SIGN_BMFB2.equals(rt.getNodeDefineKey())
                            ||FLOW_SIGN_BMFB3.equals(rt.getNodeDefineKey()) ||FLOW_SIGN_BMFB4.equals(rt.getNodeDefineKey())){
                        setMapValue(dataMap, "未分办", rt, existList, orgId);
                    }else{
                        setMapValue(dataMap, userId, rt, existList, orgId);
                    }
                }
            }

            if (Validate.isObject(resultMap)) {
                resultMap.put("XTYPE", "USER");
            }
        }
        //遍历map,如果是部长则查人员，否则查部门
        Map<String, Map<String, Object>> histogramMap = new LinkedHashMap<>();
        if (authFlag == 3) {
            Map<String, Object> deptMap = null,notDealMap = null;
            for (Map.Entry<String, Map<String, Object>> entry : dataMap.entrySet()) {
                //如果是人才显示人名，统计数量的不显示
                if(!"COUNT_TASK_MAP".equals(entry.getKey())){
                    if("部门协办".equals(entry.getKey())){
                        deptMap = entry.getValue();
                    }else if("未分办".equals(entry.getKey())){
                        notDealMap = entry.getValue();
                    }else{
                        User user = userService.getCacheUserById(entry.getKey());
                        Map<String, Object> runTaskInfoMap = entry.getValue();
                        runTaskInfoMap.put("HISTOGRAM_NAME", user.getDisplayName());
                        histogramMap.put(user.getDisplayName(), runTaskInfoMap);
                    }

                }
            }
            if(Validate.isMap(deptMap)){
                deptMap.put("HISTOGRAM_NAME", "部门协办");
                histogramMap.put("部门协办", deptMap);
            }
            if(Validate.isMap(notDealMap)){
                notDealMap.put("HISTOGRAM_NAME", "未分办");
                histogramMap.put("未分办", notDealMap);
            }
        } else {
            List<OrgDept> allOrgDept = authMap.get("allOrgDeptList") == null ? orgDeptService.queryAll() : (List<OrgDept>) authMap.get("allOrgDeptList");
            for (OrgDept orgDept : allOrgDept) {
                if (dataMap.get(orgDept.getId()) != null) {
                    //综合部
                    if (Constant.OrgType.ORGZHB.getKey().equals(orgDept.getName())) {
                        Map<String, Object> runTaskInfoMap = dataMap.get(orgDept.getId());
                        runTaskInfoMap.put("HISTOGRAM_NAME", Constant.OrgType.ORGZHB.getKey());
                        histogramMap.put(Constant.OrgType.ORGZHB.getKey(), runTaskInfoMap);
                    }
                    //评估一部
                    if (Constant.OrgType.ORGPGYB.getKey().equals(orgDept.getName())) {
                        Map<String, Object> runTaskInfoMap = dataMap.get(orgDept.getId());
                        runTaskInfoMap.put("HISTOGRAM_NAME", Constant.OrgType.ORGPGYB.getKey());
                        histogramMap.put(Constant.OrgType.ORGPGYB.getKey(), runTaskInfoMap);
                    }
                    //评估二部
                    if (Constant.OrgType.ORGPGEB.getKey().equals(orgDept.getName())) {
                        Map<String, Object> runTaskInfoMap = dataMap.get(orgDept.getId());
                        runTaskInfoMap.put("HISTOGRAM_NAME", Constant.OrgType.ORGPGEB.getKey());
                        histogramMap.put(Constant.OrgType.ORGPGEB.getKey(), runTaskInfoMap);
                    }
                    //评估一部信息化
                    if (Constant.OrgType.ORXXHZ.getKey().equals(orgDept.getName())) {
                        Map<String, Object> runTaskInfoMap = dataMap.get(orgDept.getId());
                        runTaskInfoMap.put("HISTOGRAM_NAME", Constant.OrgType.ORXXHZ.getKey());
                        histogramMap.put(Constant.OrgType.ORXXHZ.getKey(), runTaskInfoMap);
                    }
                    //概算一部
                    if (Constant.OrgType.ORGGSYB.getKey().equals(orgDept.getName())) {
                        Map<String, Object> runTaskInfoMap = dataMap.get(orgDept.getId());
                        runTaskInfoMap.put("HISTOGRAM_NAME", Constant.OrgType.ORGGSYB.getKey());
                        histogramMap.put(Constant.OrgType.ORGGSYB.getKey(), runTaskInfoMap);
                    }
                    //概算二部
                    if (Constant.OrgType.ORGGSEB.getKey().equals(orgDept.getName())) {
                        Map<String, Object> runTaskInfoMap = dataMap.get(orgDept.getId());
                        runTaskInfoMap.put("HISTOGRAM_NAME", Constant.OrgType.ORGGSEB.getKey());
                        histogramMap.put(Constant.OrgType.ORGGSEB.getKey(), runTaskInfoMap);
                    }
                }
            }
            if (dataMap.get(unWorkId) != null) {
                Map<String, Object> runTaskInfoMap = dataMap.get(unWorkId);
                runTaskInfoMap.put("HISTOGRAM_NAME", "未分办");
                histogramMap.put("未分办", runTaskInfoMap);
            }
        }
        //在办项目统计信息
        if(dataMap.get("COUNT_TASK_MAP") != null){
            histogramMap.put("COUNT_TASK_MAP",dataMap.get("COUNT_TASK_MAP"));
        }
        return histogramMap;
    }

    private void setMapValue(Map<String, Map<String, Object>> dataMap, String key, RuProcessTask runTask, List<String> existKey, String orgId) {
        Map<String, Object> runTaskInfoMap = dataMap.get(key);
        List<RuProcessTask> runTaskList = null;
        //业务ID
        String businessKey = runTask.getBusinessKey();

        //key+业务ID 确保唯一
        String mergeKey = key + businessKey;
        int count = 0;
        boolean haveNew = false;
        if (runTaskInfoMap == null) {
            haveNew = true;
            runTaskInfoMap = new HashMap<>();
            count = 1;
            runTaskList = new ArrayList<>();
        } else {
            if (!existKey.contains(mergeKey)) {
                haveNew = true;
                runTaskList = (List<RuProcessTask>) runTaskInfoMap.get("TASK_LIST");
                count = Integer.parseInt(runTaskInfoMap.get("COUNT").toString()) + 1;
            }
        }
        if (haveNew) {
            runTaskInfoMap.put("COUNT", count);
            runTaskInfoMap.put("TASK_LIST", runTaskList);
            RuProcessTask countTask = new RuProcessTask();
            BeanCopierUtils.copyProperties(runTask, countTask);
            //如果是部门，只取该部门下的负责人
            if (Validate.isString(orgId)) {
                countTask.setAllPriUser(signPrincipalService.prinUserName(countTask.getBusinessKey(), orgId));
            }
            runTaskList.add(countTask);
            dataMap.put(key, runTaskInfoMap);
            existKey.add(mergeKey);
        }
        //计算在办项目个数
        Map<String,Object> countTaskMap = dataMap.get("COUNT_TASK_MAP");
        if(countTaskMap == null){
            countTaskMap = new HashMap<>();
        }
        countTaskMap.put(businessKey,runTask);
        dataMap.put("COUNT_TASK_MAP",countTaskMap);
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
