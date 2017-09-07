package cs.service.topic;

import cs.common.Constant;
import cs.common.FlowConstant;
import cs.common.ResultMsg;
import cs.common.utils.*;
import cs.domain.flow.FlowPrincipal;
import cs.domain.sys.User;
import cs.domain.topic.TopicInfo;
import cs.domain.topic.TopicInfo_;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.topic.TopicInfoDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.flow.FlowPrincipalRepo;
import cs.repository.repositoryImpl.sys.UserRepo;
import cs.repository.repositoryImpl.topic.TopicInfoRepo;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private FlowPrincipalRepo flowPrincipalRepo;

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
        boolean updatePriUser = true;
        if (!Validate.isString(record.getId())) {
            record.setId(UUID.randomUUID().toString());
        }else{
            //如果已经发起流程，则不能修改项目负责人
            if(Validate.isString(record.getProcessInstanceId())){
                updatePriUser = false;
            }else{
                flowPrincipalRepo.deletePriUserByUserId(record.getId());
            }
        }

        TopicInfo domain = new TopicInfo();
        BeanCopierUtils.copyProperties(record, domain);
        Date now = new Date();
        domain.setCreatedBy(SessionUtil.getDisplayName());
        domain.setModifiedBy(SessionUtil.getDisplayName());
        domain.setCreatedDate(now);
        domain.setModifiedDate(now);
        topicInfoRepo.save(domain);
        //可以修改流程负责人
        if(updatePriUser){
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
        }
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
        //2、判断是否已经发起流程，如果未发起，则发起流程
        if (!Validate.isString(record.getProcessInstanceId())) {
            if (!Validate.isString(record.getId())) {
                record.setId(UUID.randomUUID().toString());
            }
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(FlowConstant.TOPIC_FLOW, record.getId(),
                    ActivitiUtil.setAssigneeValue(FlowConstant.FlowParams.USER.getValue(), SessionUtil.getUserId()));
            processEngine.getRuntimeService().setProcessInstanceName(processInstance.getId(), record.getTopicName());
            record.setProcessInstanceId(processInstance.getId());
            //自动跳过填报环节
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().singleResult();
            taskService.addComment(task.getId(), processInstance.getId(), "");
            //部长
            User directorUser = userRepo.getCacheUserById(SessionUtil.getUserInfo().getOrg().getOrgDirector());
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
        flowPrincipalRepo.deletePriUserByUserId(id);

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
        return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！");
    }

}