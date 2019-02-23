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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cs.common.constants.Constant.ERROR_MSG;

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

    //@RequiresPermissions("sign#fingByOData#post")
    @RequiresAuthentication
    @RequestMapping(name = "获取收文数据", path = "fingByOData", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDto> get(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        return signService.get(odataObj);
    }

    @RequiresAuthentication
    @RequestMapping(name = "获取项目取回数据", path = "fingByGetBack", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<RuProcessTask> getBackList(HttpServletRequest request) throws ParseException {
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
        return signService.getBackList(odataObj, isOrgLeader);
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#getBack#post")
    @RequestMapping(name = "项目重新分办", path = "getBack", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ResultMsg getBack(@RequestParam String taskId, String businessKey) {
        String backActivityId = "", branchId = "";
        if (SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.VICE_DIRECTOR.getValue())) {
            backActivityId = FlowConstant.FLOW_SIGN_FGLD_FB;
        } else {
            OrgDept orgDept = orgDeptRepo.findById(OrgDept_.directorID.getName(), SessionUtil.getUserId());
            if (Validate.isObject(orgDept) && SessionUtil.hashRole(Constant.EnumFlowNodeGroupName.DEPT_LEADER.getValue())) {
                //根据当前用户所在部门ID，查询是哪个分支的取回
                SignBranch signBranch = signBranchService.findByOrgDirector(businessKey, orgDept.getId());
                if (signBranch != null) {
                    branchId = signBranch.getBranchId();
                    if(Validate.isString(branchId)){
                        switch (Integer.parseInt(branchId)){
                            case 1:
                                backActivityId = FlowConstant.FLOW_SIGN_BMFB1;
                                break;
                            case 2:
                                backActivityId = FlowConstant.FLOW_SIGN_BMFB2;
                                break;
                            case 3:
                                backActivityId = FlowConstant.FLOW_SIGN_BMFB3;
                                break;
                            case 4:
                                backActivityId = FlowConstant.FLOW_SIGN_BMFB4;
                                break;
                                default:
                                    ;
                        }
                    }
                } else {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，你没有权限进行此操作！");
                }
            }
        }
        if (Validate.isString(backActivityId)) {
            try {
                ResultMsg result = flowService.callBackProcess(taskId, backActivityId, businessKey, Validate.isString(branchId) ? false : true);
                //取回成功,则删除相应的分支信息
                if (result.isFlag()) {
                    signService.deleteBranchInfo(businessKey, branchId);
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
    public UnitScoreDto findSignUnitScore(@RequestParam String signId){
        return signService.findSignUnitScore(signId);
    }

    //@RequiresPermissions("sign#findBySignUser#post")
    @RequiresAuthentication
    @RequestMapping(name = "签收人项目列表", path = "findBySignUser", method = RequestMethod.POST)
    @ResponseBody
    public PageModelDto<SignDto> findBySignUser(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        return signService.findBySignUser(odataObj);
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#findAssociateSign#post")
    @RequestMapping(name = "获取待关联的项目", path = "findAssociateSign", method = RequestMethod.POST)
    @ResponseBody
    public List<SignDispaWork> findAssociateSign(@RequestBody SignDispaWork signDispaWork) {
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
    @ResponseBody
    public ResultMsg update(@RequestBody SignDto signDto) {
        return signService.updateSign(signDto);
    }

    //@RequiresPermissions("sign##post")
    //@LogMsg(module = "保存收文")
    @RequiresAuthentication
    @RequestMapping(name = "创建收文", path = "", method = RequestMethod.POST)
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
        ResultMsg resultMsg = signService.deleteSign(signid);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
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
    @ResponseBody
    public ResultMsg initFillPageData(@RequestParam String signid) {
        ResultMsg resultMsg = signService.initFillPageData(signid);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "初始化详情页面", path = "html/initDetailPageData", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public SignDto initDetailPageData(@RequestParam String signid,@RequestParam(defaultValue = "false", required = false) boolean queryAll) {
        SignDto signDto = signService.findById(signid, queryAll);
        if(!Validate.isObject(signDto)){
            signDto = new SignDto();
        }
        return signDto;
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#selectSign#get")
    @RequestMapping(name = "获取办事处信息", path = "selectSign", method = RequestMethod.GET)
    @ResponseBody
    public List<OrgDto> selectSign(HttpServletRequest request) throws ParseException {
        ODataObj odataObj = new ODataObj(request);
        List<OrgDto> orgDto = signService.selectSign(odataObj);
        if(!Validate.isList(orgDto)){
            orgDto = new ArrayList<>();
        }
        return orgDto;
    }

    @RequiresAuthentication
    @RequestMapping(name = "查找项目概算", path = "findAssistSign", method = RequestMethod.GET)
    @ResponseBody
    public List<SignDto> findAssistSign() {
        List<SignDto> resultList = signService.findAssistSign();
        if(!Validate.isList(resultList)){
            resultList = new ArrayList<>();
        }
        return resultList;
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#initSignList#get")
    @RequestMapping(name = "初始化项目查询统计", path = "initSignList", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg initSignList() {
        ResultMsg resultMsg = signService.initSignList();
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "初始化流程处理页面", path = "initFlowPageData", method = RequestMethod.GET)
    @Transactional
    @ResponseBody
    public SignDto initFlowPageData(@RequestParam String signid) {
        SignDto signDto = signService.findById(signid, true);
        if(!Validate.isObject(signDto)){
            signDto = new SignDto();
        }
        return signDto;
    }

    /**
     * 统计项目平均天数，未办结的按当前日期算，已办结的按办结日期算
     * @param signIds
     * @return
     */
    @RequiresAuthentication
    @RequestMapping(name = "统计项目平均天数", path = "sumExistDays", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg sumExistDays(@RequestParam String signIds){
        ResultMsg resultMsg = signService.sumExistDays(signIds);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "通过signId获取平均评审天数和工作日" , path = "findAVGDayId" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg findAVGDayId(@RequestParam  String signIds){
        ResultMsg resultMsg = signService.findAVGDayId(signIds);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    //@RequiresPermissions("sign#startNewFlow#post")
    @RequestMapping(name = "发起流程", path = "startNewFlow", method = RequestMethod.POST)
    @ResponseBody
    @LogMsg(module = "发起项目签收流程",logLevel = "2")
    @Transactional
    public ResultMsg startNewFlow(@RequestParam String signid) {
        ResultMsg resultMsg = signService.startNewFlow(signid);
        if(Validate.isObject(resultMsg)){
            if(resultMsg.isFlag()){
                String procInstName = Validate.isObject(resultMsg.getReObj())?resultMsg.getReObj().toString():"";
                rtxService.dealPoolRTXMsg(null,resultMsg.getIdCode(),resultMsg,procInstName,Constant.MsgType.project_type.name());
                resultMsg.setIdCode(null);
                resultMsg.setReObj(null);
            }
        }else{
            resultMsg = ResultMsg.error(Constant.ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "正式签收", path = "realSign", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg realSign(@RequestParam String signid) {
        ResultMsg resultMsg = signService.realSign(signid);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name="在维护项目中添加评审部门" , path = "addAOrg" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg addOrg(@RequestParam String signId, @RequestParam String orgIds){
        ResultMsg resultMsg = signService.addAOrg(signId, orgIds);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "移除项目维护中所添加的评审部门" , path = "deleteOrg" , method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg deleteOrg(@RequestParam String signId, @RequestParam String orgIds){
        ResultMsg resultMsg = signService.deleteAOg(signId , orgIds);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "添加项目维护中的添加负责人" , path = "addSecondUser" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg addSecondUser(@RequestParam String signId , @RequestParam String userId){
        ResultMsg resultMsg = signService.addSecondUser(signId ,  userId);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "删除项目维护中添加的负责人" , path = "deleteSecondUser" , method = RequestMethod.DELETE)
    @ResponseBody
    public ResultMsg deleteSecondUser(@RequestParam String signId ,  @RequestParam String userId){
        ResultMsg resultMsg = signService.deleteSecondUser(signId ,  userId);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "保存是否能自选多个专家" , path = "saveMoreExpert" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg saveMoreExpert(@RequestParam String signId , @RequestParam String isMoreExpert){
        ResultMsg resultMsg = signService.saveMoreExpert(signId , isMoreExpert);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
    }

    @RequiresAuthentication
    @RequestMapping(name = "更新回传发改委状态" , path = "updateSendFGWState" , method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg updateSendFGWState(@RequestParam String signId , @RequestParam String state){
        ResultMsg resultMsg = signService.updateSendFGWState(signId , state);
        if(!Validate.isObject(resultMsg)){
            resultMsg = ResultMsg.error(ERROR_MSG);
        }
        return resultMsg;
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
