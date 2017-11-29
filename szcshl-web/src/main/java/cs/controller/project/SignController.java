package cs.controller.project;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import cs.ahelper.MudoleAnnotation;
import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.utils.SessionUtil;
import cs.domain.flow.RuProcessTask;
import cs.domain.project.*;
import cs.domain.sys.OrgDept;
import cs.domain.sys.OrgDept_;
import cs.listener.SysStartUpListener;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.service.flow.FlowService;
import cs.service.project.SignBranchService;
import cs.service.project.WorkProgramService;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cs.common.ResultMsg;
import cs.common.utils.Validate;
import cs.model.PageModelDto;
import cs.model.project.SignDto;
import cs.model.sys.OrgDto;
import cs.repository.odata.ODataObj;
import cs.service.project.SignService;

@Controller
@RequestMapping(name = "收文", path = "sign")
@MudoleAnnotation(name = "项目管理", value = "permission#sign")
public class SignController {
    private static Logger logger = Logger.getLogger(SignController.class);
    String ctrlName = "sign";
    @Autowired
    private SignService signService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private SignBranchService signBranchService;
    @Autowired
    private OrgDeptRepo orgDeptRepo;

    //@RequiresPermissions("sign#fingByOData#post")
    @RequiresAuthentication
    @RequestMapping(name = "获取收文数据", path = "fingByOData", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<SignDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDto> signDtos = signService.get(odataObj);
        return signDtos;
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取项目取回数据", path = "fingByGetBack", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<RuProcessTask> getBackList(HttpServletRequest request) throws ParseException {
        PageModelDto<RuProcessTask> signDispaWork = new PageModelDto<>();
        //是否为部长或者小组组长
        boolean isOrgLeader = false;
        if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())
                || null != orgDeptRepo.findById(OrgDept_.directorID.getName(), SessionUtil.getUserId())) {
            isOrgLeader = true;
        } else if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())) {
            isOrgLeader = false;
        } else {
            return signDispaWork;
        }
        ODataObj odataObj = new ODataObj(request);
        signDispaWork = signService.getBackList(odataObj, isOrgLeader);
        return signDispaWork;
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#getBack#post")
    @RequestMapping(name = "项目重新分办", path = "getBack", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ResultMsg getBack(@RequestParam(required = true) String taskId, String businessKey) {
        String backActivityId = "", branch = "";
        if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())) {
            backActivityId = FlowConstant.FLOW_SIGN_FGLD_FB;
        } else {
            OrgDept orgDept = orgDeptRepo.findById(OrgDept_.directorID.getName(), SessionUtil.getUserId());
            if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())
                    || null != orgDept) {
                //根据当前用户所在部门ID，查询是哪个分支的取回
                SignBranch signBranch = signBranchService.findByOrgDirector(businessKey, orgDept.getId());
                if (signBranch != null) {
                    branch = signBranch.getBranchId();
                    if (FlowConstant.SignFlowParams.BRANCH_INDEX1.getValue().equals(branch)) {
                        backActivityId = FlowConstant.FLOW_SIGN_BMFB1;
                    } else if (FlowConstant.SignFlowParams.BRANCH_INDEX2.getValue().equals(branch)) {
                        backActivityId = FlowConstant.FLOW_SIGN_BMFB2;
                    } else if (FlowConstant.SignFlowParams.BRANCH_INDEX3.getValue().equals(branch)) {
                        backActivityId = FlowConstant.FLOW_SIGN_BMFB3;
                    } else if (FlowConstant.SignFlowParams.BRANCH_INDEX4.getValue().equals(branch)) {
                        backActivityId = FlowConstant.FLOW_SIGN_BMFB4;
                    }
                } else {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，你没有权限进行此操作！");
                }
            }
        }
        if (Validate.isString(backActivityId)) {
            ResultMsg result = null;
            try {
                result = flowService.callBackProcess(taskId, backActivityId, businessKey, Validate.isString(branch) ? false : true);
                //取回成功,则删除相应的分支信息
                if (result.isFlag()) {
                    signService.deleteBranchInfo(businessKey, Validate.isString(branch) ? branch : null);
                }
                return result;
            } catch (Exception e) {
                logger.info("项目签收流程取回异常：" + e.getMessage());
                return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "流程回退失败，请联系系统管理员查看！");
            }
        } else {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，你没有权限进行此操作！");
        }
    }

    //@RequiresPermissions("sign#findBySignUser#post")
    @RequiresAuthentication
    @RequestMapping(name = "签收人项目列表", path = "findBySignUser", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<SignDto> findBySignUser(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDto> signDtos = signService.findBySignUser(odataObj);
        return signDtos;
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#findAssociateSign#post")
    @RequestMapping(name = "获取待关联的项目", path = "findAssociateSign", method = RequestMethod.POST)
    public @ResponseBody
    List<SignDispaWork> findAssociateSign(@RequestBody SignDispaWork signDispaWork) {
        return signService.findAssociateSign(signDispaWork);
    }

    //编辑收文
    //@RequiresPermissions("sign##put")
    @RequiresAuthentication
    @RequestMapping(name = "更新收文", path = "", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody SignDto signDto) {
        signService.updateSign(signDto);
    }

    //@RequiresPermissions("sign##post")
    @RequiresAuthentication
    @RequestMapping(name = "创建收文", path = "", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg post(@RequestBody SignDto signDto) {
        return signService.createSign(signDto);
    }

    @RequiresAuthentication
    @RequestMapping(name = "项目预签收", path = "html/reserveAddPost", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg reserveAddPost(@RequestBody SignDto signDto) {
        return signService.reserveAddSign(signDto);
    }

    @RequiresAuthentication
//    @RequiresPermissions("sign#html/reserveAdd#get")
    @RequestMapping(name = "项目预签收", path = "html/reserveAdd", method = RequestMethod.GET)
    public String reserveAdd() {
        return ctrlName + "/reserveAdd";
    }

    @RequiresPermissions("sign#html/list#get")
    @RequestMapping(name = "项目签收列表", path = "html/list", method = RequestMethod.GET)
    public String list() {
        return ctrlName + "/list";
    }

    @RequiresAuthentication
//    @RequiresPermissions("sign#html/add#get")
    @RequestMapping(name = "项目签收", path = "html/add", method = RequestMethod.GET)
    public String add() {
        return ctrlName + "/add";
    }


    @RequiresAuthentication
//    @RequiresPermissions("sign#html/reserveList#get")
    @RequestMapping(name = "项目预签收列表", path = "html/reserveList", method = RequestMethod.GET)
    public String reserveList() {

        return ctrlName + "/reserveList";
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#reserveListSign#post")
    @RequestMapping(name = "获取预签收列表", path = "reserveListSign", method = RequestMethod.POST)
    public @ResponseBody
    PageModelDto<SignDto> reserveListSign(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        PageModelDto<SignDto> signlist = signService.findAllReserve(odataObj);
        return signlist;
    }

    /**
     * 获取项目关联
     *
     * @param signId 项目ID
     */
    @RequiresAuthentication
    //@RequiresPermissions("sign#associate#get")
    @RequestMapping(name = "获取已关联阶段项目", path = "associate", method = RequestMethod.GET)
    public @ResponseBody
    List<SignDto> associateGet(@RequestParam(required = true) String signId) {
        return signService.getAssociateDtos(signId);

    }

    /**
     * 项目关联
     *
     * @param signId      项目ID
     * @param associateId 关联到的项目ID
     */
    @RequiresAuthentication
    //@RequiresPermissions("sign#associate#post")
    @RequestMapping(name = "项目关联", path = "associate", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public @ResponseBody
    ResultMsg associatePost(@RequestParam(required = true) String signId, String associateId) {
        return signService.associate(signId, associateId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据协审计划查询收文信息", path = "findByPlanId", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> findByPlanId(@RequestParam(required = true) String planId,@RequestParam(defaultValue = "0") String isOnlySign) {
        return signService.findByPlanId(planId,isOnlySign);
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#html/fillin#get")
    @RequestMapping(name = "填写表格页面", path = "html/fillin", method = RequestMethod.GET)
    public String fillin() {
        return ctrlName + "/fillin";
    }

    //收文详细信息
    @RequiresAuthentication
    //@RequiresPermissions("sign#html/signDetails#get")
    @RequestMapping(name = "收文详细信息", path = "html/signDetails", method = RequestMethod.GET)
    public String signDetails() {

        return ctrlName + "/signDetails";
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign##delete")
    @RequestMapping(name = "删除收文", path = "", method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg deleteSign(@RequestParam String signid) {
        return signService.deleteSign(signid);
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#deleteReserve#delete")
    @RequestMapping(name = "删除预签收收文", path = "deleteReserve", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteReserve(@RequestParam String signid) {
        signService.deleteReserveSign(signid);
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#html/initFillPageData#post")
    @RequestMapping(name = "初始收文编辑页面", path = "html/initFillPageData", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg initFillPageData(@RequestParam(required = true) String signid) {
        return signService.initFillPageData(signid);
    }

    @RequiresAuthentication
    @RequestMapping(name = "初始化详情页面", path = "html/initDetailPageData", method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    SignDto initDetailPageData(@RequestParam(required = true) String signid,
                               @RequestParam(defaultValue = "false", required = false) boolean queryAll) {
        return signService.findById(signid, queryAll);
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#selectSign#get")
    @RequestMapping(name = "获取办事处信息", path = "selectSign", method = RequestMethod.GET)
    public @ResponseBody
    List<OrgDto> selectSign(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        List<OrgDto> orgDto = signService.selectSign(odataObj);
        return orgDto;
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#initSignList#get")
    @RequestMapping(name = "初始化项目查询统计", path = "initSignList", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg initSignList() {
        return signService.initSignList();
    }

    @RequiresAuthentication
    @RequestMapping(name = "初始化流程处理页面", path = "initFlowPageData", method = RequestMethod.GET)
    @Transactional
    public @ResponseBody
    SignDto initFlowPageData(@RequestParam(required = true) String signid) {
        return signService.findById(signid, true);
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#startNewFlow#post")
    @RequestMapping(name = "发起流程", path = "startNewFlow", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg startNewFlow(@RequestParam(required = true) String signid) {
        return signService.startNewFlow(signid);
    }

    @RequiresAuthentication
    @RequestMapping(name = "正式签收", path = "realSign", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg realSign(@RequestParam(required = true) String signid) {
        return signService.realSign(signid);
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#html/flowDeal#get")
    @RequestMapping(name = "项目流程处理", path = "html/flowDeal", method = RequestMethod.GET)
    public String flowDeal() {

        return ctrlName + "/flowDeal";
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#html/signFlowDetail#get")
    @RequestMapping(name = "项目流程详情", path = "html/signFlowDetail", method = RequestMethod.GET)
    public String signFlowDetail() {

        return ctrlName + "/flowDetail";
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#html/signEndDetails#get")
    @RequestMapping(name = "项目流程处理", path = "html/signEndDetails", method = RequestMethod.GET)
    public String signEndDetails() {

        return ctrlName + "/signEndDetails";
    }

    @RequiresAuthentication
//    @RequiresPermissions("sign#html/ruProcessTask#get")
    @RequestMapping(name = "在办项目", path = "html/ruProcessTask", method = RequestMethod.GET)
    public String ruProcessTask() {

        return ctrlName + "/ruProcessTask";
    }

    @RequiresAuthentication
//    @RequiresPermissions("sign#html/hiProcessTask#get")
    @RequestMapping(name = "已办项目", path = "html/hiProcessTask", method = RequestMethod.GET)
    public String hiProcessTask() {

        return ctrlName + "/hiProcessTask";
    }

    @RequiresPermissions("sign#html/getBack#get")
    @RequestMapping(name = "项目重新分办", path = "html/signGetBack", method = RequestMethod.GET)
    public String getBack() {

        return ctrlName + "/signGetBack";
    }

    @RequiresAuthentication
    @RequestMapping(name = "查找项目概算", path = "findAssistSign", method = RequestMethod.GET)
    public @ResponseBody
    List<SignDto> findAssistSign() {

        return signService.findAssistSign();
    }

    @RequiresPermissions("sign#personDtasks#get")
    @RequestMapping(name = "个人在审项目", path = "personDtasks")
    public String personDtasks(Model model) {

        return "admin/personDtasks";
    }

    @RequiresPermissions("sign#etasks#get")
    @RequestMapping(name = "办结项目", path = "etasks")
    public String etasks(Model model) {

        return "admin/etasks";
    }

}
