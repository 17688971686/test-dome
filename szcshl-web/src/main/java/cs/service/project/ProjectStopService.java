package cs.service.project;

import cs.common.ResultMsg;
import cs.domain.project.ProjectStop;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.flow.FlowDto;
import cs.model.project.ProjectStopDto;
import cs.repository.odata.ODataObj;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;

public interface ProjectStopService {
	
	List<ProjectStop> findProjectStopBySign(String signId);

    SignDispaWork findSignBySignId(String signId);

    ResultMsg savePauseProject(ProjectStopDto projectStopDto);

    PageModelDto<ProjectStopDto> findProjectStopByStopId(ODataObj oDataObj);

    /**
     * 根据ID获取项目暂停信息
     * @param stopId
     * @return
     */
    ProjectStopDto getProjectStopByStopId(String stopId);

    List<ProjectStop> findPauseProjectSuccess();

    void updateProjectStopStatus(List<ProjectStop> projectStopList);

    /**
     * 项目暂停审批流程处理
     * @param processInstance
     * @param task
     * @param flowDto
     * @return
     */
    ResultMsg dealFlow(ProcessInstance processInstance, Task task, FlowDto flowDto);

    /**
     * 通过收文ID获取审批通过的项目暂停信息
     * @param signId
     * @return
     */
    List<ProjectStopDto> getStopList(String signId);
}
