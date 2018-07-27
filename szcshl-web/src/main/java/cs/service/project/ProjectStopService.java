package cs.service.project;

import cs.common.ResultMsg;
import cs.domain.project.ProjectStop;
import cs.domain.project.Sign;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.project.ProjectStopDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;

public interface ProjectStopService {

    List<ProjectStopDto> findProjectStopBySign(String signId);

    SignDispaWork findSignBySignId(String signId);

    ResultMsg savePauseProject(ProjectStopDto projectStopDto);

    ResultMsg saveProjectStop(ProjectStopDto projectStopDto);

    PageModelDto<ProjectStopDto> findProjectStopByStopId(ODataObj oDataObj);

    /**
     * 根据ID获取项目暂停信息
     *
     * @param stopId
     * @return
     */
    ProjectStopDto getProjectStopByStopId(String stopId);

    /**
     * 查询正在执行的暂停项目
     *
     * @return
     */
    List<ProjectStop> selectPauseProject();

    /**
     * 批量更新暂停信息
     *
     * @param projectStopList
     */
    void updateProjectStopStatus(List<ProjectStop> projectStopList);

    /**
     * 项目暂停审批流程处理
     *
     * @param processInstance
     * @param task
     * @param flowDto
     * @return
     */
    ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto);

    /**
     * 通过收文ID获取审批通过的项目暂停信息
     *
     * @param signId
     * @return
     */
    List<ProjectStopDto> getStopList(String signId);

    /**
     * 根据项目暂停ID获取项目信息
     *
     * @param stopid
     * @return
     */
    Sign findSignByStopId(String stopid);

    /**
     * 删除流程
     * @param businessKey
     * @return
     */
    ResultMsg endFlow(String businessKey);
}
