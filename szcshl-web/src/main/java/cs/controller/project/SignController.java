package cs.controller.project;

import cs.ahelper.LogMsg;
import cs.ahelper.MudoleAnnotation;
import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.ResultMsg;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.flow.RuProcessTask;
import cs.domain.project.SignBranch;
import cs.domain.project.SignDispaWork;
import cs.domain.sys.OrgDept;
import cs.domain.sys.OrgDept_;
import cs.model.PageModelDto;
import cs.model.project.SignDto;
import cs.model.project.UnitScoreDto;
import cs.model.sys.OrgDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.service.flow.FlowService;
import cs.service.project.SignBranchService;
import cs.service.project.SignService;
import cs.service.rtx.RTXService;
import cs.service.sys.SMSContent;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private RTXService rtxService;
    @Autowired
    private SMSContent smsContent;

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

    @RequiresAuthentication
    @RequestMapping(name = "获取评分单位信息", path = "findSignUnitScore", method = RequestMethod.POST)
    @ResponseBody
    public UnitScoreDto findSignUnitScore(@RequestParam(required = true) String signId) throws ParseException {
        return  signService.findSignUnitScore(signId);
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


    @RequiresAuthentication
    @RequestMapping(name = "获取待关联的项目列表", path = "findAssociateSignList", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDispaWork> findAssociateSignList( String signid, String reviewstage, String projectname,String mUserName, String skip, String size) {
        return signService.findAssociateSignList(signid, reviewstage, projectname, mUserName,skip, size);
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
    @LogMsg(module = "保存收文")
    @ResponseBody
    public ResultMsg post(@RequestBody SignDto signDto) {
        return signService.createSign(signDto);
    }

    @RequiresAuthentication
    @RequestMapping(name = "项目预签收", path = "html/reserveAddPost", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg reserveAddPost(@RequestBody SignDto signDto) {
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
   @RequiresPermissions("sign#html/reserveList#get")
    @RequestMapping(name = "项目预签收列表", path = "html/reserveList", method = RequestMethod.GET)
    public String reserveList() {

        return ctrlName + "/reserveList";
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#reserveListSign#post")
    @RequestMapping(name = "获取预签收列表", path = "reserveListSign", method = RequestMethod.POST)
    @ResponseBody
    public  PageModelDto<SignDto> reserveListSign(HttpServletRequest request) throws ParseException {
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
    @ResponseBody
    public ResultMsg associatePost(@RequestParam(required = true) String signId, String associateId) {
        return signService.associate(signId, associateId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "根据协审计划查询收文信息", path = "findByPlanId", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> findByPlanId(@RequestParam(required = true) String planId,@RequestParam(defaultValue = "0") String isOnlySign) {
        return signService.findByPlanId(planId,isOnlySign);
    }

    /**
     * 恢复项目
     *
     * @param signId      项目ID
     * @param stateProperty 项目状态字段
     * @param stateValue 状态值
     */
    @RequiresAuthentication
    @RequestMapping(name = "恢复项目", path = "editSignState", method = RequestMethod.POST)
    public @ResponseBody
    ResultMsg editSignState(@RequestParam(required = true) String signId, String stateProperty,String stateValue) {
        return signService.editSignState(signId, stateProperty,stateValue);
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

/*    @RequiresAuthentication
    //@RequiresPermissions("sign#deleteReserve#delete")
    @RequestMapping(name = "删除预签收收文", path = "deleteReserve", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteReserve(@RequestParam String signid) {
        signService.deleteReserveSign(signid);
    }*/

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
    @ResponseBody
    public ResultMsg initSignList() {
        return signService.initSignList();
    }

    @RequiresAuthentication
    @RequestMapping(name = "初始化流程处理页面", path = "initFlowPageData", method = RequestMethod.GET)
    @Transactional
    @ResponseBody
    public SignDto initFlowPageData(@RequestParam(required = true) String signid) {
        return signService.findById(signid, true);
    }

    /**
     * 统计项目平均天数，未办结的按当前日期算，已办结的按办结日期算
     * @param signIds
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(name = "统计项目平均天数", path = "sumExistDays", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg sumExistDays(@RequestParam(required = true) String signIds){
        return signService.sumExistDays(signIds);
    }

    @RequiresAuthentication
    @RequestMapping(name = "通过signId获取平均评审天数和工作日" , path = "findAVGDayId" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg findAVGDayId(@RequestParam  String signIds){
        return signService.findAVGDayId(signIds);
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#startNewFlow#post")
    @RequestMapping(name = "发起流程", path = "startNewFlow", method = RequestMethod.POST)
    @ResponseBody
    @LogMsg(module = "发起项目签收流程",logLevel = "2")
    @Transactional
    public ResultMsg startNewFlow(@RequestParam(required = true) String signid) {
        ResultMsg resultMsg = signService.startNewFlow(signid);
        if(resultMsg.isFlag()){
            ProcessInstance processInstance = (ProcessInstance) resultMsg.getReObj();
            rtxService.dealPoolRTXMsg(resultMsg.getIdCode(),resultMsg,processInstance,smsContent.get("项目",processInstance.getName()));
            resultMsg.setIdCode(null);
            resultMsg.setReObj(null);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "正式签收", path = "realSign", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg realSign(@RequestParam(required = true) String signid) {
        return signService.realSign(signid);
    }


    @RequiresAuthentication
    @RequestMapping(name="在维护项目中添加评审部门" , path = "addAOrg" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg addOrg(@RequestParam String signId , @RequestParam String orgIds ){
        return signService.addAOrg(signId , orgIds );
    }

    @RequiresAuthentication
    @RequestMapping(name = "移除项目维护中所添加的评审部门" , path = "deleteOrg" , method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg deleteOrg(@RequestParam String signId , @RequestParam String orgIds){
        return signService.deleteAOg(signId , orgIds);
    }

    @RequiresAuthentication
    @RequestMapping(name = "添加项目维护中的添加负责人" , path = "addSecondUser" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg addSecondUser(@RequestParam String signId , @RequestParam String userId){
        return signService.addSecondUser(signId ,  userId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "删除项目维护中添加的负责人" , path = "deleteSecondUser" , method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg deleteSecondUser(@RequestParam String signId ,  @RequestParam String userId){
        return signService.deleteSecondUser(signId ,  userId);
    }

    @RequiresAuthentication
    @RequestMapping(name = "保存是否能自选多个专家" , path = "saveMoreExpert" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveMoreExpert(@RequestParam String signId , @RequestParam String isMoreExpert){
        return signService.saveMoreExpert(signId , isMoreExpert);
    }

    @RequiresAuthentication
    @RequestMapping(name = "更新回传发改委状态" , path = "updateSendFGWState" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg updateSendFGWState(@RequestParam String signId , @RequestParam String state){
        return signService.updateSendFGWState(signId , state);
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

    @RequiresPermissions("sign#html/signGetBack#get")
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
    @RequestMapping(name = "个人经办项目", path = "personDtasks")
    public String personDtasks(Model model) {

        return "admin/personDtasks";
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#etasks#get")
    @RequestMapping(name = "办结项目", path = "etasks")
    public String etasks(Model model) {

        return "admin/etasks";
    }
    
    /***************报审登记表导出***************/
   /* @RequiresAuthentication
    @RequestMapping(name="报审登记表导出" , path = "printSign" , method = RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.NO_CONTENT)
    public void printSign(HttpServletResponse resp, @RequestParam String signId , @RequestParam String reviewStage){


        InputStream is = null;
        ServletOutputStream sos = null;
        File file = signService.printSign(signId , reviewStage);
        try {
            String title = java.net.URLDecoder.decode(reviewStage, "UTF-8");//解码，需要抛异常
            String fileName = reviewStage + "报审登记表";
            is = new FileInputStream(file);
            resp.setCharacterEncoding("utf-8");
            resp.setContentType("application/msword");
            // 设置浏览器以下载的方式处理该文件默认
            String fileName2 = new String((fileName + ".doc").getBytes("GB2312"), "ISO-8859-1");
            resp.addHeader("Content-Disposition", "attachment;filename=" + fileName2);
            sos = resp.getOutputStream();
            byte[] buffer = new byte[512];  // 缓冲区
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = is.read(buffer)) != -1) {
                sos.write(buffer, 0, bytesToRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }*/

}
