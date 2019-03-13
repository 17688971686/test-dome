package cs.service.topic;

import cs.common.*;
import cs.common.constants.Constant;
import cs.common.constants.FlowConstant;
import cs.common.constants.ProjectConstant;
import cs.common.utils.*;
import cs.domain.expert.ExpertReview;
import cs.domain.flow.FlowPrincipal;
import cs.domain.project.AddRegisterFile;
import cs.domain.project.AddRegisterFile_;
import cs.domain.sys.OrgDept;
import cs.domain.sys.User;
import cs.domain.topic.*;
import cs.model.PageModelDto;
import cs.model.expert.ExpertReviewDto;
import cs.model.flow.FlowDto;
import cs.model.project.AddRegisterFileDto;
import cs.model.topic.*;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertReviewRepo;
import cs.repository.repositoryImpl.flow.FlowPrincipalRepo;
import cs.repository.repositoryImpl.meeting.RoomBookingRepo;
import cs.repository.repositoryImpl.project.AddRegisterFileRepo;
import cs.repository.repositoryImpl.sys.OrgDeptRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.repository.repositoryImpl.topic.*;
import cs.service.flow.FlowService;
import cs.service.rtx.RTXSendMsgPool;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

import static cs.common.constants.SysConstants.SUPER_ACCOUNT;


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
    @Autowired
    private WorkPlanService workPlanService;
    @Autowired
    private ExpertReviewRepo expertReviewRepo;
    @Autowired
    private RoomBookingRepo roomBookingRepo;
    @Autowired
    private FlowService flowService;
    @Autowired
    private ContractRepo contractRepo;
    @Autowired
    private TopicMaintainRepo topicMaintainRepo;

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public PageModelDto<TopicInfoDto> get(ODataObj odataObj) {
        PageModelDto<TopicInfoDto> pageModelDto = new PageModelDto<TopicInfoDto>();
        List<TopicInfoDto> resultDtoList = new ArrayList<>();

        List<TopicInfo> resultList = topicInfoRepo.findByOdata(odataObj);
        if (Validate.isList(resultList)) {
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
        } else {
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
        if (!Validate.isString(domain.getTopicCode())) {
            String yearString = DateUtils.converToString(domain.getCreatedDate(), DateUtils.DATE_YEAR);
            int maxSeq = topicInfoRepo.findCurMaxSeq(yearString) + 1;
            if(maxSeq < 1){
                maxSeq = 1;
            }
            String fileNum = maxSeq > 999 ? maxSeq + "" : String.format("%03d", maxSeq);
            domain.setTopicCode(yearString + ProjectConstant.FILE_RECORD_KEY.KT.toString() + fileNum);
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

    /***
     * 更新课题信息
     * @param record
     * @return
     */
    @Override
    public ResultMsg updateTopic(TopicInfoDto record){
        TopicInfo domain = new TopicInfo();
        BeanCopierUtils.copyProperties(record, domain);
        Date now = new Date();
        domain.setCreatedBy(SessionUtil.getUserId());
        domain.setModifiedBy(SessionUtil.getUserId());
        domain.setCreatedDate(now);
        domain.setModifiedDate(now);
        topicInfoRepo.save(domain);
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
        ProcessInstance processInstance = null;
        Task task = null;
        //1、判断有没有选择项目负责人
        if (!Validate.isString(record.getMainPrinUserId())) {
            return ResultMsg.error( "操作失败，请先选择项目负责人！");
        }
        //2、没有默认部门，则不能发起流程
        if (!Validate.isString(record.getOrgId())) {
            return ResultMsg.error( "您不是相关部门人员，不能发起流程！");
        }
        //3、判断是否已经发起流程，如果未发起，则发起流程
        if (!Validate.isString(record.getProcessInstanceId())) {
            if (!Validate.isString(record.getId())) {
                record.setId(UUID.randomUUID().toString());
            }
             processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.TOPIC_FLOW, record.getId(),
                    ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER.getValue(), SessionUtil.getUserId()));
            processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), record.getTopicName());
            //设置状态和实例ID
            record.setProcessInstanceId(processInstance.getId());
            record.setState(Constant.EnumState.PROCESS.getValue());
            //自动跳过填报环节
            task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            taskService.addComment(task.getId(), processInstance.getId(), "");
            //部长
            OrgDept orgDept = orgDeptRepo.findOrgDeptById(record.getOrgId());
            User directorUser = userRepo.getCacheUserById(orgDept.getDirectorID());
            taskService.complete(task.getId(), ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_BZ.getValue(),
                    Validate.isString(directorUser.getTakeUserId()) ? directorUser.getTakeUserId() : directorUser.getId()));
            //放入腾讯通消息缓冲池
            RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), Validate.isString(directorUser.getTakeUserId()) ? directorUser.getTakeUserId() : directorUser.getId());
        }
        ResultMsg resultMsg = save(record);
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
        if(resultMsg.isFlag()){
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),task.getId(),"操作成功！",processDefinitionEntity.getName());
        }else{
              return ResultMsg.error("操作失败！");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TopicInfoDto record) {
        TopicInfo domain = topicInfoRepo.findById(record.getId());
        if(Validate.isObject(domain) && Validate.isString(domain.getId())){
            BeanCopierUtils.copyProperties(record, domain);
            domain.setModifiedBy(SessionUtil.getDisplayName());
            domain.setModifiedDate(new Date());
            topicInfoRepo.save(domain);
        }else{
            throw new IllegalArgumentException("操作失败，记录数据已被删除！");
        }
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

        //合同
        if (Validate.isList(domain.getContractList())) {
            List<Contract> contractList = domain.getContractList();
            List<ContractDto> contractDtoList = new ArrayList<>(domain.getContractList().size());
            contractList.forEach(contract -> {
                ContractDto contractDto = new ContractDto();
                BeanCopierUtils.copyProperties(contract, contractDto);
                contractDtoList.add(contractDto);
            });
            modelDto.setContractDtoList(contractDtoList);
        }
        //工作方案
        if (domain.getWorkPlan() != null) {
            WorkPlan workPlan = domain.getWorkPlan();
            WorkPlanDto workPlanDto = new WorkPlanDto();
            BeanCopierUtils.copyProperties(workPlan, workPlanDto);
            workPlanDto = workPlanService.findLinkBusiness(workPlan, workPlanDto);
            modelDto.setWorkPlanDto(workPlanDto);
        }
        //归档
        if (domain.getFiling() != null) {
            Filing filing = domain.getFiling();
            FilingDto filingDto = new FilingDto();
            BeanCopierUtils.copyProperties(filing, filingDto);
            //查询补充资料函信息
            List<AddRegisterFile> registerFileList = addRegisterFileRepo.findByIds(AddRegisterFile_.businessId.getName(), filing.getId(), null);
            if (Validate.isList(registerFileList)) {
                List<AddRegisterFileDto> dtoList = new ArrayList<>(registerFileList.size());
                registerFileList.forEach(rgf -> {
                    AddRegisterFileDto dto = new AddRegisterFileDto();
                    BeanCopierUtils.copyProperties(rgf, dto);
                    dtoList.add(dto);
                });
                filingDto.setRegisterFileDto(dtoList);
            }
            modelDto.setFilingDto(filingDto);
        }
        //专家评审方案
        ExpertReview expertReview = expertReviewRepo.findByBusinessId(id);
        if (Validate.isObject(expertReview)) {
            ExpertReviewDto expertReviewDto = expertReviewRepo.formatReview(expertReview);
            modelDto.setExpertReviewDto(expertReviewDto);
        }
        return modelDto;
    }

    /**
     * 删除操作
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ResultMsg delete(String id) {
        TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), id);
        if (Validate.isString(topicInfo.getProcessInstanceId())) {
            return ResultMsg.error( "您已经发起了流程，不能对数据进行删除！");
        }
        topicInfoRepo.deleteById(TopicInfo_.id.getName(), id);
        flowPrincipalRepo.deletePriUserByBusiId(id);

        return ResultMsg.ok("删除成功！");
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override
    public TopicInfoDto findDetailById(String id) {
        TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), id);
        if (topicInfo == null) {
            return null;
        }
        TopicInfoDto topicInfoDto = new TopicInfoDto();
        BeanCopierUtils.copyProperties(topicInfo, topicInfoDto);
        //合同
        if (Validate.isList(topicInfo.getContractList())) {
            List<Contract> contractList = topicInfo.getContractList();
            List<ContractDto> contractDtoList = new ArrayList<>(topicInfo.getContractList().size());
            contractList.forEach(contract -> {
                ContractDto contractDto = new ContractDto();
                BeanCopierUtils.copyProperties(contract, contractDto);
                contractDtoList.add(contractDto);
            });
            topicInfoDto.setContractDtoList(contractDtoList);
        }
        //获取项目负责人
        List<FlowPrincipal> list = flowPrincipalRepo.getFlowPrinInfoByBusiId(id);
        if (Validate.isList(list)) {
            list.forEach(l -> {
                if (Constant.EnumState.YES.getValue().equals(l.getIsMainUser())) {
                    topicInfoDto.setMainPrinUserId(l.getUserId());
                } else {
                    topicInfoDto.setPrinUserIds(topicInfoDto.getPrinUserIds() + l.getUserId() + ",");
                }
            });
        }
        return topicInfoDto;
    }

    /**
     * 流程处理
     *
     * @param processInstance
     * @param flowDto
     * @return ResultMsg
     */
    @Override
    @Transactional
    public ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto) {
        String businessId = processInstance.getBusinessKey(),
                assigneeValue = "";                             //流程处理人
        Map<String, Object> variables = null;                   //流程参数
        User dealUser = null;                                  //用户
        List<User> dealUserList = null;                        //用户列表
        TopicInfo topicInfo = null;                            //课题研究
        WorkPlan workPlan = null;                              //工作方案
        Filing filing = null;                                  //资料归档
        boolean isNextUser = false;                 //是否是下一环节处理人（主要是处理领导审批，部长审批）
        String nextNodeKey = "";                    //下一环节名称
        //环节处理人设定
        switch (task.getTaskDefinitionKey()) {
            //部长审核计划
            case FlowConstant.TOPIC_BZSH_JH:
                variables = findOrgLeader(businessId, true, assigneeValue);
                //下一环节还是自己处理
                if (assigneeValue.equals(SessionUtil.getUserId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.TOPIC_FGLD_JH;
                }
                break;
            //分管领导审核计划
            case FlowConstant.TOPIC_FGLD_JH:
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(dealUserList)) {
                    return ResultMsg.error( "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                dealUser = dealUserList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_ZR.getValue(), assigneeValue);

                //下一环节还是自己处理
                if (assigneeValue.equals(SessionUtil.getUserId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.TOPIC_ZRSH_JH;
                }
                break;
            //主任审核计划
            case FlowConstant.TOPIC_ZRSH_JH:
                variables = findMainPrinUser(businessId, assigneeValue);
                break;
      /*      //报发改委
            case FlowConstant.TOPIC_BFGW:
                variables = findPrinUser(businessId, assigneeValue);
                break;*/

           /* //联系合作单位
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
                break;*/
            //提出成果鉴定会
/*            case FlowConstant.TOPIC_GZFA:
                workPlan = workPlanRepo.findById("topId", businessId);
                if (workPlan == null || !Validate.isString(workPlan.getId())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您还没完成工作方案，不能进行下一步操作！");
                }
                //更改预定会议室状态
                roomBookingRepo.updateStateByBusinessId(workPlan.getId(), Constant.EnumState.PROCESS.getValue());
                variables = findOrgLeader(businessId, false, assigneeValue);
                break;*/
            //课题负责人选结题
            case FlowConstant.TOPIC_KTFZR:
                topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), businessId);
                if(!Validate.isString(topicInfo.getEndTopicFlag())){
                    return ResultMsg.error("请设置结题方式！");
                }
                if(!Validate.isString(topicInfo.getSendFgw())){
                    return ResultMsg.error( "请设置是否报委务会审定！");
                }
                variables = findOrgLeader(businessId, false, assigneeValue);
                break;
            //部长审核方案
            case FlowConstant.TOPIC_BZSH_FA:
                variables = findOrgLeader(businessId, true, assigneeValue);
                break;
            //分管副主任审核方案
            case FlowConstant.TOPIC_FGLD_FA:
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(dealUserList)) {
                    return ResultMsg.error( "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                dealUser = dealUserList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_ZR.getValue(), assigneeValue);
                //下一环节还是自己处理
                if (assigneeValue.equals(SessionUtil.getUserId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.TOPIC_ZRSH_FA;
                }
                break;
            //主任审定
            case FlowConstant.TOPIC_ZRSH_FA:
                topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), businessId);
                topicInfo.setApprovedDate(new Date());
                topicInfoRepo.save(topicInfo);
                variables = findMainPrinUser(businessId, assigneeValue);
                break;
            //课题负责人确认
            case FlowConstant.TOPIC_KTFZR_QR:
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.FILER.getValue());
                if (!Validate.isList(dealUserList)) {
                    return ResultMsg.error( "请先设置【" + Constant.EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                }
                filing = filingRepo.findById("topId", businessId);
                if (filing == null || !Validate.isString(filing.getId())) {
                    return ResultMsg.error( "您还没完成课题归档，不能进行下一步操作！");
                }
                topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), businessId);
                topicInfoRepo.save(topicInfo);
                dealUser = dealUserList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_GDY.getValue(), assigneeValue);
                //下一环节还是自己处理
                if (assigneeValue.equals(SessionUtil.getUserId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.TOPIC_ZLGD;
                }
                break;
            //归档员确认
            case FlowConstant.TOPIC_ZLGD:
            /*    filing = filingRepo.findById("topId", businessId);
                filingRepo.save(filing);*/
                topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), businessId);
                topicInfo.setState(Constant.EnumState.YES.getValue());
                topicInfoRepo.save(topicInfo);
                break;
            default:
                ;
                //完成课题报告
      /*      case FlowConstant.TOPIC_KTBG:
                variables = findOrgLeader(businessId, false, assigneeValue);
                break;*/
                //部长审核
          /*  case FlowConstant.TOPIC_BZSH_BG:
                variables = findOrgLeader(businessId, true, assigneeValue);
                break;*/
                //分管副主任审核
         /*   case FlowConstant.TOPIC_FGLD_BG:
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(dealUserList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                dealUser = dealUserList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_ZR.getValue(), assigneeValue);
                //下一环节还是自己处理
                if (assigneeValue.equals(SessionUtil.getUserId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.TOPIC_ZRSH_BG;
                }
                break;
            //主任审核
            case FlowConstant.TOPIC_ZRSH_BG:
                variables = findPrinUser(businessId, assigneeValue);
                break;
            //课题结题
            case FlowConstant.TOPIC_KTJT:
                topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), businessId);
                topicInfo.setEndTime(new Date());
                topicInfoRepo.save(topicInfo);
                variables = findOrgLeader(businessId, false, assigneeValue);
                break;
            //部长审核
            case FlowConstant.TOPIC_BZSH_JT:
                variables = findOrgLeader(businessId, true, assigneeValue);
                break;
            //分管副主任审核
            case FlowConstant.TOPIC_FGLD_JT:
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.DIRECTOR.getValue());
                if (!Validate.isList(dealUserList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.DIRECTOR.getValue() + "】角色用户！");
                }
                dealUser = dealUserList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_ZR.getValue(), assigneeValue);
                //下一环节还是自己处理
                if (assigneeValue.equals(SessionUtil.getUserId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.TOPIC_ZRSH_JT;
                }
                break;
            //主任审核
            case FlowConstant.TOPIC_ZRSH_JT:
                variables = findPrinUser(businessId, assigneeValue);
                break;*/
            /*//印发资料
            case FlowConstant.TOPIC_YFZL :
                variables = findMainPrinUser(businessId,assigneeValue);
                break;*/
                //资料归档
            /*case FlowConstant.TOPIC_ZLGD:
                filing = filingRepo.findById("topId", businessId);
                if (filing == null || !Validate.isString(filing.getId())) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您还没完成课题归档，不能进行下一步操作！");
                }
                //如果没有完成专家评分，则不可以提交到下一步
                if (!expertReviewRepo.isFinishEPGrade(businessId)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您还未对专家进行评分,不能提交到下一步操作！");
                }
                //如果有专家评审费，则要先办理专家评审费
                if (expertReviewRepo.isHaveEPReviewCost(businessId)) {
                    ExpertReview expertReview = expertReviewRepo.findByBusinessId(businessId);
                    if (expertReview.getPayDate() == null || expertReview.getTotalCost() == null) {
                        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "您还没完成专家评审费发放，不能进行下一步操作！");
                    }
                }
                variables = findOrgLeader(businessId, false, assigneeValue);
                break;
            //部长审核归档
            case FlowConstant.TOPIC_BZSH_GD:
                dealUserList = userRepo.findUserByRoleName(Constant.EnumFlowNodeGroupName.FILER.getValue());
                if (!Validate.isList(dealUserList)) {
                    return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(), "请先设置【" + Constant.EnumFlowNodeGroupName.FILER.getValue() + "】角色用户！");
                }
                dealUser = dealUserList.get(0);
                assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
                variables = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_GDY.getValue(), assigneeValue);
                //下一环节还是自己处理
                if (assigneeValue.equals(SessionUtil.getUserId())) {
                    isNextUser = true;
                    nextNodeKey = FlowConstant.TOPIC_GDY_QR;
                }
                filing = filingRepo.findById("topId", businessId);
                filing.setDirector(SessionUtil.getDisplayName());
                filingRepo.save(filing);
                break;*/
        }

        taskService.addComment(task.getId(), processInstance.getId(), (flowDto == null) ? "" : flowDto.getDealOption());    //添加处理信息
        if (flowDto.isEnd()) {
            taskService.complete(task.getId());
        } else {
            taskService.complete(task.getId(), variables);
            //如果下一环节还是自己
            if (isNextUser) {
                List<Task> nextTaskList = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(assigneeValue).list();
                for (Task t : nextTaskList) {
                    if (nextNodeKey.equals(t.getTaskDefinitionKey())) {
                        return dealFlow(processInstance, t, flowDto);

                    }
                }
            }
        }
        //放入腾讯通消息缓冲池
        RTXSendMsgPool.getInstance().sendReceiverIdPool(task.getId(), assigneeValue);

        //当下一个处理人还是自己的时候，任务ID是已经改变了的，所以这里要返回任务ID
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),task.getId(), "操作成功！",null);
    }

    /**
     * 处理发改委返回的结果
     *
     * @param resultMsg
     * @param topicInfoDto
     * @return
     */
    @Override
    public ResultMsg dealReturnAudit(ResultMsg resultMsg, TopicInfoDto topicInfoDto) {
        ResultMsg resultObj = null;
        try {

            if (topicInfoDto == null) {
                return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_TOPIC_01.getCode(), IFResultCode.IFMsgCode.SZEC_TOPIC_01.getValue());
            }
            if (!Validate.isString(topicInfoDto.getProcessInstanceId())) {
                return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_TOPIC_02.getCode(), IFResultCode.IFMsgCode.SZEC_TOPIC_02.getValue());
            }
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(topicInfoDto.getProcessInstanceId()).singleResult();
            Task task = null;
            if (Validate.isString(topicInfoDto.getTaskId())) {
                task = taskService.createTaskQuery().taskId(topicInfoDto.getTaskId()).active().singleResult();
            } else {
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            }
            if (task == null) {
                return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_TOPIC_03.getCode(), IFResultCode.IFMsgCode.SZEC_TOPIC_03.getValue());
            }
            //方案通过，进行下一步处理
            if (resultMsg.isFlag() || "01".equals(resultMsg.getReCode())) {
                FlowDto flowDto = new FlowDto();
                flowDto.setDealOption(resultMsg.getReMsg());
                return dealFlow(processInstance, task, flowDto);
            } else {
                switch (resultMsg.getReCode()) {
                    case "02":              //方案修改,流程回退
                        FlowDto flowDto = new FlowDto();
                        flowDto.setTaskId(task.getId());
                        flowDto.setDealOption(resultMsg.getReMsg());
                        resultObj = flowService.rollBackLastNode(flowDto);
                        break;
                    case "03":              //方案不通过，直接终止
                        topicInfoDto.setState(Constant.EnumState.FORCE.getValue());
                        save(topicInfoDto);
                        runtimeService.deleteProcessInstance(processInstance.getId(), resultMsg.getReMsg());
                        resultObj = new ResultMsg(true, Constant.MsgCode.OK.getValue(), "操作成功！");
                        break;
                    default:
                        ;
                }
            }
            return resultObj;
        } catch (Exception e) {
            return new ResultMsg(false, IFResultCode.IFMsgCode.SZEC_DEAL_ERROR.getCode(), IFResultCode.IFMsgCode.SZEC_DEAL_ERROR.getValue());
        }
    }

    /**
     * 获取部门领导
     *
     * @param assigneeValue
     * @param businessId
     * @param isSLeader(true:返回分管领导，false:返回部门负责人)
     * @return
     */
    private Map<String, Object> findOrgLeader(String businessId, boolean isSLeader, String assigneeValue) {
        Map<String, Object> resultMap = null;
        TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), businessId);
        OrgDept orgDept = orgDeptRepo.findOrgDeptById(topicInfo.getOrgId());
        if (isSLeader) {
            User dealUser = userRepo.getCacheUserById(orgDept.getsLeaderID());
            assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
            resultMap = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_FGLD.getValue(), assigneeValue);
            return resultMap;
        } else {
            User dealUser = userRepo.getCacheUserById(orgDept.getDirectorID());
            assigneeValue = Validate.isString(dealUser.getTakeUserId()) ? dealUser.getTakeUserId() : dealUser.getId();
            resultMap = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER_BZ.getValue(), assigneeValue);
            return resultMap;
        }
    }

    /**
     * 获取项目负责人
     *
     * @param businessId
     * @param assigneeValue
     * @return
     */
    private Map<String, Object> findPrinUser(String businessId, String assigneeValue) {
        Map<String, Object> resultMap = null;
        List<User> dealUserList = flowPrincipalRepo.getFlowAllPrinByBusiId(businessId);
        for (User pu : dealUserList) {
            if (Validate.isString(assigneeValue)) {
                assigneeValue += ",";
            }
            assigneeValue += Validate.isString(pu.getTakeUserId()) ? pu.getTakeUserId() : pu.getId();
        }
        resultMap = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USERS.getValue(), assigneeValue);
        return resultMap;
    }

    /**
     * 获取项目负责人
     *
     * @param businessId
     * @param assigneeValue
     * @return
     */
    private Map<String, Object> findMainPrinUser(String businessId, String assigneeValue) {
        Map<String, Object> resultMap = null;
        User user = flowPrincipalRepo.getFlowMainPrinByBusiId(businessId);
        assigneeValue = Validate.isString(user.getTakeUserId()) ? user.getTakeUserId() : user.getId();
        resultMap = ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER.getValue(), assigneeValue);
        return resultMap;
    }

    /***
     * 保存合同信息
     * @param contractDtoList
     * @return
     */
    @Override
    public ResultMsg saveContractDetailList(ContractDto[] contractDtoList) {
        List<Contract> contractList = new ArrayList<Contract>();
        TopicInfo topicInfo = null;
        if (Validate.isString(contractDtoList[0].getTopicId())) {
            topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(), contractDtoList[0].getTopicId());
        }
        for (int i = 0, l = contractDtoList.length; i < l; i++) {
            Contract contract = new Contract();
            ContractDto contractDto = contractDtoList[i];
            BeanCopierUtils.copyProperties(contractDto, contract);
            contract.setContractId(UUID.randomUUID().toString());
            contract.setCreatedBy(SessionUtil.getDisplayName());
            contract.setCreatedDate(new Date());
            contract.setModifiedBy(SessionUtil.getDisplayName());
            contract.setModifiedDate(new Date());
            if (Validate.isString(contractDto.getTopicId())) {
                if (Validate.isObject(topicInfo)) {
                    contract.setTopicInfo(topicInfo);
                }
            }
            contractList.add(contract);
        }
        if (contractList.size() > 0) {
            if (null != topicInfo.getContractList()){
                topicInfo.getContractList().clear();
                topicInfo.getContractList().addAll(contractList);
            }else{
                topicInfo.setContractList(contractList);
            }
            topicInfoRepo.save(topicInfo);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "保存成功！", topicInfo);
        }else{
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"没有分录数据，无法保存！");
        }

    }

    /**
     * 保存课题维护信息
     * @param topicMaintainDtoArray
     * @return
     */
    @Override
    public ResultMsg saveTopicDetailList(TopicMaintainDto[] topicMaintainDtoArray){
        List<TopicMaintain> topicList = new ArrayList<TopicMaintain>();
        TopicInfo topicInfo = null;

        for (int i = 0, l = topicMaintainDtoArray.length; i < l; i++) {
            TopicMaintain topicMaintain = new TopicMaintain();
            TopicMaintainDto topicMaintainDto = topicMaintainDtoArray[i];
            BeanCopierUtils.copyProperties(topicMaintainDto, topicMaintain);
            if(!Validate.isString(topicMaintain.getId())){
                topicMaintain.setId(UUID.randomUUID().toString());
                if(Validate.isString(topicMaintain.getTopicId())){
                    topicInfo = topicInfoRepo.getById(topicMaintain.getTopicId());
                    if(Validate.isObject(topicInfo)){
                        topicInfo.setIsMaintain("9");
                        topicInfoRepo.save(topicInfo);
                    }
                }
            }
            topicMaintain.setUserId(SessionUtil.getUserId());
            topicMaintain.setCreatedBy(SessionUtil.getDisplayName());
            topicMaintain.setCreatedDate(new Date());
            topicMaintain.setModifiedBy(SessionUtil.getDisplayName());
            topicMaintain.setModifiedDate(new Date());

            topicList.add(topicMaintain);
        }
        if (topicList.size() > 0) {
            topicMaintainRepo.bathUpdate(topicList);
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(), "保存成功！", topicList);
        }else{
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"没有分录数据，无法保存！");
        }
    }

    /**
     * 删除流程业务处理
     * @param businessKey
     * @return
     */
    @Override
    public ResultMsg endFlow(String businessKey) {
        TopicInfo topicInfo = topicInfoRepo.findById(TopicInfo_.id.getName(),businessKey);
        if(Validate.isObject(topicInfo)){
            if(!SessionUtil.getUserId().equals(topicInfo.getCreatedBy()) && !SUPER_ACCOUNT.equals(SessionUtil.getLoginName())){
                return ResultMsg.error("您无权进行删除流程操作！");
            }
            topicInfo.setState(Constant.EnumState.FORCE.getValue());
            topicInfoRepo.save(topicInfo);
        }
        return ResultMsg.ok("操作成功！");
    }


    /**
     * 删除合同
     * @param ids
     * @return
     */

    @Override
    @Transactional
    public ResultMsg deleteContract(String ids) {
        try{
            if(Validate.isString(ids)){
                if(ids.contains(",")){
                    String idsArr[] = ids.split(",");
                    for(String id : idsArr){
                        delExistContractInfo(id);
                    }
                }else{
                    delExistContractInfo(ids);
                }
            }

        }catch (Exception e){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"保存失败，数据异常");
        }
        return new ResultMsg(false, Constant.MsgCode.OK.getValue(),"操作成功！");
    }

    /**
     * 删除课题维护信息
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public ResultMsg deleteTopicMaintain(String ids) {
        try{
            if(Validate.isString(ids)){
                if(ids.contains(",")){
                    String idsArr[] = ids.split(",");
                    for(String id : idsArr){
                        delExistTopicInfo(id);
                    }
                }else{
                    delExistTopicInfo(ids);
                }
            }

        }catch (Exception e){
            return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"保存失败，数据异常");
        }
        return new ResultMsg(false, Constant.MsgCode.OK.getValue(),"操作成功！");
    }

    private boolean delExistContractInfo(String id){
        boolean flag = false;
        Contract contract = contractRepo.findById(Contract_.contractId.getName(), id);
        if (null != contract){
            contractRepo.delete(contract);
            flag = true;
        }
        return  flag;
    }

    private boolean delExistTopicInfo(String id){
        boolean flag = false;
        TopicMaintain topicMaintain = topicMaintainRepo.findById(TopicMaintain_.id.getName(), id);
        if (null != topicMaintain){
            topicMaintainRepo.delete(topicMaintain);
            flag = true;
        }
        if(Validate.isString(topicMaintain.getTopicId())){
            TopicInfo topicInfo = topicInfoRepo.getById(topicMaintain.getTopicId());
            if(Validate.isObject(topicInfo)){
                topicInfo.setIsMaintain("0");
                topicInfoRepo.save(topicInfo);
            }
        }

        return  flag;
    }

}