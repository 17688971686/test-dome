package cs.service.project;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.domain.project.Sign;
import cs.domain.project.WorkProgram;
import cs.model.flow.FlowDto;
import cs.model.project.WorkProgramDto;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public interface WorkProgramService {

    /**
     * 保存工作方案
     * @param workProgramDto
     * @param isNeedWorkProgram
     * @return
     */
    ResultMsg save(WorkProgramDto workProgramDto, Boolean isNeedWorkProgram);

    /**
     * 根据收文编号任务ID初始化工作方案信息
     * @param signId
     * @param taskId    主要是用于区分是自己的工作方案还是代办的工作方案
     * @return
     */
    Map<String,Object> initWorkProgram(String signId,String taskId,String brandId);

    Map<String,Object> workMaintainList(String signId);



    /**
     * 根据当前负责人，删除工作方案信息
     * @param signId
     * @return
     */
    ResultMsg deleteBySignId(String signId);

    void delete(String id);

    /**
     * 生成会前准备材料
     * @param signId
     * @return
     */
    ResultMsg createMeetingDoc(String signId);

    /**
     * 根据主键查询工作方案信息
     * @param workId
     * @return
     */
    WorkProgramDto initWorkProgramById(String workId);

    /**
     * 通过项目负责人获取项目信息
     * @param signId
     * @return
     */
    WorkProgramDto findByPrincipalUser(String signId);

    /**
     * 根据合并评审主项目ID，获取合并评审次项目的工作方案信息
     * @param signid
     * @return
     */
    List<WorkProgramDto> findMergeWP(String signid);

    /**
     * 专家评审方式修改
     * @param signId            项目ID
     * @param workprogramId     工作方案ID
     * @param reviewType        新的评审方式
     * @return
     */
    ResultMsg updateReviewType(String signId, String workprogramId, String reviewType);


    /**
     * 初始化项目的共同部分信息
     * @param workProgramDto
     * @param sign
     */
    void copySignCommonInfo(WorkProgramDto workProgramDto,Sign sign);

    /**
     * 通过业务ID判断是不是主分支
     * @param signId
     * @return
     */
    Boolean mainBranch(String signId);

    /**
     * 更新工作方案专家评审费用
     * @param wpId
     */
    void initExpertCost(String wpId);

    /**
     * 获取工作方案调研及会议信息
     * @return
     */
    Map<String,Object> findProMeetInfo();

    /**
     * 初始化项目基本信息
     * @param signId
     * @return
     */
    WorkProgramDto initBaseInfo(String signId);

    /**
     * 保存项目基本信息
     * @param workProgramDto
     * @return
     */
    ResultMsg saveBaseInfo(WorkProgramDto workProgramDto);

    /**
     * 重做工作方案
     * @param signId
     * @param brandIds  分支ID
     * @return
     */
    ResultMsg startReWorkFlow(String signId, String reworkType,String brandIds,String userId);

    /**
     * 重做工作方案流程处理
     * @param processInstance
     * @param task
     * @param flowDto
     * @return
     */
    ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto);

    /**
     * 删除流程处理
     * @param businessKey
     * @return
     */
    ResultMsg endFlow(String businessKey);
}
