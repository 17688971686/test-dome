package cs.service.reviewProjectAppraise;

import cs.common.ResultMsg;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.project.AppraiseReportDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;

/**
 * Created by MCL
 * 2017/9/23
 */
public interface AppraiseService {

    PageModelDto<SignDispaWork> findAppraisingProject(ODataObj oDataObj);

    void updateIsAppraising(String signId);

    /**
     * 保存优秀评审报告申请表
     * @param appraiseReportDto
     * @return
     */
    ResultMsg saveApply(AppraiseReportDto appraiseReportDto);

    PageModelDto<AppraiseReportDto> getAppraiseReport(ODataObj oDataObj);

    /**
     * 通过ID查询
     * @param id
     * @return
     */
    AppraiseReportDto getAppraiseById (String id);

    /**
     * 通过项目ID，初始化优秀评审报告
     * @param signId
     * @return
     */
    AppraiseReportDto initBySignId(String signId);

    /**
     * 发起流程
     * @param id
     * @return
     */
    ResultMsg startFlow(String id);

    /**
     * 流程处理
     * @param processInstance
     * @param task
     * @param flowDto
     * @return
     */
    ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto);

    /**
     * 结束流程
     * @param businessKey
     * @return
     */
    ResultMsg endFlow(String businessKey);
}
