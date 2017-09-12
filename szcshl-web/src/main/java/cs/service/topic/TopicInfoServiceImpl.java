package cs.service.topic;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.HqlBuilder;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.flow.FlowPrincipal;
import cs.domain.project.AddRegisterFile;
import cs.domain.project.AddRegisterFile_;
import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.domain.topic.Filing;
import cs.domain.topic.TopicInfo;
import cs.domain.topic.TopicInfo_;
import cs.domain.topic.WorkPlan;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.project.AddRegisterFileDto;
import cs.model.topic.FilingDto;
import cs.model.topic.TopicInfoDto;
import cs.model.topic.WorkPlanDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.flow.FlowPrincipalRepo;
import cs.repository.repositoryImpl.project.AddRegisterFileRepo;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.repository.repositoryImpl.topic.FilingRepo;
import cs.repository.repositoryImpl.topic.TopicInfoRepo;
import cs.repository.repositoryImpl.topic.WorkPlanRepo;
import cs.service.rtx.RTXSendMsgPool;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static cs.common.Constant.SUPER_USER;

/**
 * Description: 课题研究 业务操作实现类
 * author: sjy
 * Date: 2017-9-4 15:04:55
 */
@Service
public class TopicInfoServiceImpl implements TopicInfoService {

    @Autowired
    private TopicInfoRepo topicInfoRepo;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrgDeptRepo orgDeptRepo;
    @Autowired
    private FlowPrincipalRepo flowPrincipalRepo;
    @Autowired
    private AddRegisterFileRepo addRegisterFileRepo;
    @Autowired
    private WorkPlanRepo workPlanRepo;
    @Autowired
    private FilingRepo filingRepo;

    @Override
    public PageModelDto<TopicInfoDto> get(ODataObj odataObj) {
        PageModelDto<TopicInfoDto> pageModelDto = new PageModelDto<TopicInfoDto>();
        List<TopicInfo> resultList = topicInfoRepo.findByOdata(odataObj);
        List<TopicInfoDto> resultDtoList = new ArrayList<TopicInfoDto>(resultList.size());

        if (resultList != null && resultList.size() > 0) {
            resultList.forEach(x -> {
                TopicInfoDto modelDto = new TopicInfoDto();
                BeanCopierUtils.copyProperties(x, modelDto);

                resultDtoList.add(modelDto);
            });
        }
        pageModelDto.setCount(odataObj.getCount());
        pageModelDto.setValue(resultDtoList);
        return pageModelDto;
    }

    /**
     * 保存
     *
     * @param record
     * @return
     */
    @Override
    @Transactional
    public ResultMsg save(TopicInfoDto record) {
        if (!Validate.isString(record.getId())) {
            record.setId(UUID.randomUUID().toString());
        }else{
            flowPrincipalRepo.deletePriUserByBusiId(record.getId());
        }

        TopicInfo domain = new TopicInfo();
        BeanCopierUtils.copyProperties(record, domain);
        Date now = new Date();
        domain.setCreatedBy(SessionUtil.getUserId());
        domain.setModifiedBy(SessionUtil.getUserId());
        domain.setCreatedDate(now);
        domain.setModifiedDate(now);
        //课题代码(课题代码2017KT001，归档编号2016KD17001)
        if(!Validate.isString(domain.getTopicCode())){
            String yearString = DateUtils.converToString(domain.getCreatedDate(),"yyyy");
            String fileNumValue;
            int maxSeq = findCurMaxSeq(yearString);
            if(maxSeq < 1000){
                fileNumValue = String.format("%03d", Integer.valueOf(maxSeq+1));
            }else{
                fileNumValue = (maxSeq+1)+"";
            }
            domain.setTopicCode(yearString+FlowConstant.CodeKey.TOPIC_INFO_CODE.getValue()+fileNumValue);
        }
        topicInfoRepo.save(domain);
        //修改流程负责人
        List<FlowPrincipal> flPrinUserList = new ArrayList<>();
        FlowPrincipal mainFP = new FlowPrincipal();
        mainFP.setBusiId(record.getId());
        mainFP.setUserId(record.getMainPrinUserId());
        mainFP.setIsMainUser(Constant.EnumState.YES.getValue());
        flPrinUserList.add(mainFP);
        if (Validate.isString(record.getPrinUserIds())) {
            List<String> userIds = StringUtil.getSplit(record.getPrinUserIds(), ",");
            for (String id : userIds) {
                FlowPrincipal fp = new FlowPrincipal();
                fp.setBusiId(record.getId());
                fp.setUserId(id);
                fp.setIsMainUser(Constant.EnumState.NO.getValue());
                flPrinUserList.add(fp);
            }
        }
        flowPrincipalRepo.bathUpdate(flPrinUserList);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功", record);
    }

    /**
     * 发起流程
     *
     * @param record
     * @return
     */
    @Override
    @Transactional
    public ResultMsg startFlow(TopicInfoDto record) {
        //1、判断有没有选择项目负责人
        if (!Validate.isString(record.getMainPrinUserId())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "操作失败，请先选择项目负责人！");
        }
        //2、没有默认部门，则不能发起流程
        if (!Validate.isString(record.getOrgId())) {
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您不是相关部门人员，不能发起流程！");
        }
        //3、判断是否已经发起流程，如果未发起，则发起流程
        if (!Validate.isString(record.getProcessInstanceId())) {
            if (!Validate.isString(record.getId())) {
                record.setId(UUID.randomUUID().toString());
            }
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.TOPIC_FLOW, record.getId(),
                    ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER.getValue(), SessionUtil.getUserId()));
            processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), record.getTopicName());
            //设置状态和实例ID
            record.setProcessInstanceId(processInstance.getId());
            record.setState(Constant.EnumState.PROCESS.getValue());
            //自动跳过填报环节
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            taskService.addComment(task.getId(), processInstance.getId(), "");
            //部长
            OrgDept orgDept = orgDeptRepo.findOrgDeptById(record.getOrgId());
            User directorUser = userRepo.getCacheUserById(orgDept.getDirectorID());
            taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_BZ.getValue(),
                    Validate.isString(directorUser.getTakeUserId()) ? directorUser.getTakeUserId() : directorUser.getId()));
        }

        return save(record);
    }

    @Override
    @Transactional
    public void update(TopicInfoDto record) {
        TopicInfo domain = topicInfoRepo.findById(record.getId());
        BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
        domain.setModifiedBy(SessionUtil.getDisplayName());
        domain.setModifiedDate(new Date());

        topicInfoRepo.save(domain);
    }

    /**
     * 根据主键查询课题信息（包含工作方案、归档等信息）
     *
     * @param id
     * @return
     */
    @Override
    public TopicInfoDto findById(String id) {
        TopicInfoDto modelDto = new TopicInfoDto();
        TopicInfo domain = topicInfoRepo.findById(id);
        BeanCopierUtils.copyProperties(domain, modelDto);
        //工作方案
        if(domain.getWorkPlan() != null){
            WorkPlan workPlan = domain.getWorkPlan();
            WorkPlanDto workPlanDto = new WorkPlanDto();
            BeanCopierUtils.copyProperties(workPlan,workPlanDto);
            modelDto.setWorkPlanDto(workPlanDto);
        }
        //归档
        if(domain.getFiling() != null){
            Filing filing = domain.getFiling();
            FilingDto filingDto = new FilingDto();
            BeanCopierUtils.copyProperties(filing,filingDto);
            //查询补充资料函信息
            List<AddRegisterFile> registerFileList = addRegisterFileRepo.findByIds(AddRegisterFile_.businessId.getName(),filing.getId(),null);
            if(Validate.isList(registerFileList)){
                List<AddRegisterFileDto> dtoList = new ArrayList<>(registerFileList.size());
                registerFileList.forEach(rgf -> {
                    AddRegisterFileDto dto = new AddRegisterFileDto();
                    BeanCopierUtils.copyProperties(rgf,dto);
                    dtoList.add(dto);
                });
                filingDto.setRegisterFileDto(dtoList);
            }
            modelDto.setFilingDto(filingDto);

        }
        return modelDto;
    }

    /**
     * 删除操作
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ResultMsg delete(String id) {
        TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(),id);
        if(Validate.isString(topicInfo.getProcessInstanceId())){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"您已经发起了流程，不能对数据进行删除！");
        }
        topicInfoRepo.deleteById(TopicInfo_.id.getName(),id);
        flowPrincipalRepo.deletePriUserByBusiId(id);

        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"删除成功！");
    }

    /**
     * 查询详情
     * @param id
     * @return
     */
    @Override
    public TopicInfoDto findDetailById(String id) {
        TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(),id);
        if(topicInfo == null ){
            return null;
        }
        TopicInfoDto topicInfoDto = new TopicInfoDto();
        BeanCopierUtils.copyProperties(topicInfo,topicInfoDto);
        //获取项目负责人
        List<FlowPrincipal> list = flowPrincipalRepo.getFlowPrinInfoByBusiId(id);
        if(Validate.isList(list)){
            list.forEach(l->{
                if(Constant.EnumState.YES.getValue().equals(l.getIsMainUser())){
                    topicInfoDto.setMainPrinUserId(l.getUserId());
                }else{
                    topicInfoDto.setPrinUserIds(topicInfoDto.getPrinUserIds()+l.getUserId()+",");
                }
            });
        }
        return topicInfoDto;
    }

    /**
     * 流程处理
     * @param processInstance
     * @param flowDto
     * @return ResultMsg
     */
    @Override
    public ResultMsg dealFlow(ProcessInstance processInstance,Task task, FlowDto flowDto) {
        String businessId = processInstance.getBusinessKey(),
               assigneeValue = "";                             //流程处理人
        Map<String,Object> variables = null;                   //流程参数
        User dealUser = null;                                  //用户
        List<User> dealUserList = null;                        //用户列表
        TopicInfo topicInfo = null;                            //课题研究
        Filing filing = null;                                  //资料归档
        //环节处理人设定
        switch (task.getTaskDefinitionKey()) {
            //部长审核计划
            case FlowConstant.TOPIC_BZSH_JH:
                variables = findOrgLeader(businessId,true,assigneeValue);
                break;
            //分管领导审核计划
            case FlowConstant.TOPIC_FGLD_JH:
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(dealUserList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                dealUser = dealUserList.get(0);
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_ZR.getValue(),
                        Validate.isString(dealUser.getTakeUserId())?dealUser.getTakeUserId():dealUser.getId());
                break;
            //主任审核计划
            case FlowConstant.TOPIC_ZRSH_JH:
                topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(),businessId);
                //如果送发改委
                if(Constant.EnumState.YES.getValue().equals(topicInfo.getSendFgw())){
                    variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_ADMIN.getValue(),SUPER_USER);
                    variables.put(FlowConstant.FlowParams.SEND_FGW.getValue(), true);
                }else{
                    variables = findPrinUser(businessId,assigneeValue);
                    variables.put(FlowConstant.FlowParams.SEND_FGW.getValue(), false);
                }
                break;
            //报发改委
            case FlowConstant.TOPIC_BFGW :
                variables = findPrinUser(businessId,assigneeValue);
                break;

            //联系合作单位
            case FlowConstant.TOPIC_LXDW :
                variables = findPrinUser(businessId,assigneeValue);
                break;
            //签订合同
            case FlowConstant.TOPIC_QDHT :
                variables = findPrinUser(businessId,assigneeValue);
                break;
            //课题研究实施
            case FlowConstant.TOPIC_YJSS :
                variables = findPrinUser(businessId,assigneeValue);
                break;
            //内部初审
            case FlowConstant.TOPIC_NBCS :
                variables = findPrinUser(businessId,assigneeValue);
                break;
            //提出成果鉴定会
            case FlowConstant.TOPIC_GZFA :
                WorkPlan workPlan = workPlanRepo.findById("topId", businessId);
                if(workPlan == null || !Validate.isString(workPlan.getId())){
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您还没完成工作方案，不能进行下一步操作！");
                }
                variables = findOrgLeader(businessId,false,assigneeValue);
                break;
            //部长审核方案
            case FlowConstant.TOPIC_BZSH_FA :
                variables = findOrgLeader(businessId,true,assigneeValue);
                break;
            //分管副主任审核方案
            case FlowConstant.TOPIC_FGLD_FA :
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(dealUserList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                dealUser = dealUserList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId())?dealUser.getTakeUserId():dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_ZR.getValue(),assigneeValue);
                break;
            //主任审定
            case FlowConstant.TOPIC_ZRSH_FA :
                variables = findPrinUser(businessId,assigneeValue);
                break;
            //召开成果鉴定会
            case FlowConstant.TOPIC_CGJD :
                variables = findPrinUser(businessId,assigneeValue);
                break;
            //完成课题报告
            case FlowConstant.TOPIC_KTBG :
                variables = findOrgLeader(businessId,false,assigneeValue);
                break;
            //部长审核
            case FlowConstant.TOPIC_BZSH_BG :
                variables = findOrgLeader(businessId,true,assigneeValue);
                break;
            //分管副主任审核
            case FlowConstant.TOPIC_FGLD_BG :
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(dealUserList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                dealUser = dealUserList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId())?dealUser.getTakeUserId():dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_ZR.getValue(),assigneeValue);
                break;
            //主任审核
            case FlowConstant.TOPIC_ZRSH_BG :
                variables = findPrinUser(businessId,assigneeValue);
                break;
            //课题结题
            case FlowConstant.TOPIC_KTJT :
                topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(),businessId);
                topicInfo.setEndTime(new Date());
                topicInfoRepo.save(topicInfo);
                variables = findOrgLeader(businessId,false,assigneeValue);
                break;
            //部长审核
            case FlowConstant.TOPIC_BZSH_JT :
                variables = findOrgLeader(businessId,true,assigneeValue);
                break;
            //分管副主任审核
            case FlowConstant.TOPIC_FGLD_JT :
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(dealUserList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                dealUser = dealUserList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId())?dealUser.getTakeUserId():dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_ZR.getValue(),assigneeValue);
                break;
            //主任审核
            case FlowConstant.TOPIC_ZRSH_JT :
                variables = findPrinUser(businessId,assigneeValue);
                break;
            //印发资料
            case FlowConstant.TOPIC_YFZL :
                variables = findMainPrinUser(businessId,assigneeValue);
                break;
            //资料归档
            case FlowConstant.TOPIC_ZLGD :
                filing = filingRepo.findById("topId", businessId);
                if(filing == null || !Validate.isString(filing.getId())){
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您还没完成课题归档，不能进行下一步操作！");
                }
                variables = findOrgLeader(businessId,false,assigneeValue);
                break;
            //部长审核归档
            case FlowConstant.TOPIC_BZSH_GD :
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.FILER.getValue());
                if (!Validate.isList(dealUserList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                }
                dealUser = dealUserList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId())?dealUser.getTakeUserId():dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_GDY.getValue(),assigneeValue);

                filing = filingRepo.findById("topId", businessId);
                filing.setDirector(SessionUtil.getDisplayName());
                filingRepo.save(filing);
                break;
            //归档员确认
            case FlowConstant.TOPIC_GDY_QR :
                filing = filingRepo.findById("topId", businessId);
                filing.setFilingUser(SessionUtil.getDisplayName());
                filingRepo.save(filing);

                topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(),businessId);
                topicInfo.setState(Constant.EnumState.YES.getValue());
                topicInfoRepo.save(topicInfo);
                break;
        }

        taskService.addComment(task.getId(), processInstance.getId(),(flowDto == null)?"":flowDto.getDealOption());    //添加处理信息
        if (flowDto.isEnd()) {
            taskService.complete(task.getId());
        } else {
            taskService.complete(task.getId(), variables);
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(),assigneeValue);
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
    }

    /**
     * 获取部门领导
     * @param assigneeValue
     * @param businessId
     * @param isSLeader(true:返回分管领导，false:返回部门负责人)
     * @return
     */
    private Map<String,Object> findOrgLeader(String businessId,boolean isSLeader,String assigneeValue) {
        Map<String,Object> resultMap = null;
        TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(),businessId);
        OrgDept orgDept = orgDeptRepo.findOrgDeptById(topicInfo.getOrgId());
        if(isSLeader){
            User dealUser = userRepo.getCacheUserById(orgDept.getsLeaderID());
            assigneeValue = Validate.isString(dealUser.getTakeUserId())?dealUser.getTakeUserId():dealUser.getId();
            resultMap = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_FGLD.getValue(),assigneeValue);
            return resultMap;
        }else{
            User dealUser = userRepo.getCacheUserById(orgDept.getDirectorID());
            assigneeValue= Validate.isString(dealUser.getTakeUserId())?dealUser.getTakeUserId():dealUser.getId();
            resultMap = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_BZ.getValue(),assigneeValue);
            return resultMap;
        }
    }

    /**
     * 获取项目负责人
     * @param businessId
     * @param assigneeValue
     * @return
     */
    private Map<String,Object> findPrinUser(String businessId, String assigneeValue) {
        Map<String,Object> resultMap = null;
        List<User> dealUserList = flowPrincipalRepo.getFlowAllPrinByBusiId(businessId);
        for(User pu : dealUserList){
            if(Validate.isString(assigneeValue)){
                assigneeValue += ",";
            }
            assigneeValue += Validate.isString(pu.getTakeUserId())?pu.getTakeUserId():pu.getId();
        }
        resultMap = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USERS.getValue(),assigneeValue);
       return resultMap;
    }

    /**
     * 获取项目负责人
     * @param businessId
     * @param assigneeValue
     * @return
     */
    private Map<String,Object> findMainPrinUser(String businessId, String assigneeValue) {
        Map<String,Object> resultMap = null;
        User user = flowPrincipalRepo.getFlowMainPrinByBusiId(businessId);
        assigneeValue = Validate.isString(user.getTakeUserId())?user.getTakeUserId():user.getId();
        resultMap = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER.getValue(),assigneeValue);
        return resultMap;
    }

    /**
     * 根据归档日期，获取存最大序号
     * @param yearString
     * @return
     */
    private int findCurMaxSeq(String yearString) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select max("+ TopicInfo_.topicSeq.getName()+") from cs_topic_info where "+TopicInfo_.createdDate.getName()+" between ");
        sqlBuilder.append(" to_date(:beginTime,'yyyy-mm-dd hh24:mi:ss') and to_date(:endTime,'yyyy-mm-dd hh24:mi:ss' )");
        sqlBuilder.setParam("beginTime", yearString+"-01-01 00:00:00");
        sqlBuilder.setParam("endTime", yearString+"-12-31 23:59:59");
        return topicInfoRepo.returnIntBySql(sqlBuilder);
    }
}