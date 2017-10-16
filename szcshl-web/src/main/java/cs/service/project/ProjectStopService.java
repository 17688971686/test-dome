package cs.service.project;

import cs.domain.project.ProjectStop;
import cs.domain.project.SignDispaWork;
import cs.model.PageModelDto;
import cs.model.project.ProjectStopDto;
import cs.repository.odata.ODataObj;

import java.util.List;

public interface ProjectStopService {
	
	/*void addProjectStop(String signid,String taskid);
	void projectStart(String signid,String taskid);*/
	
	List<ProjectStop> findProjectStopBySign(String signId);

    SignDispaWork findSignBySignId(String signId);

    int countUsedWorkday(String signId);

    void savePauseProject(ProjectStopDto projectStopDto);

    PageModelDto<ProjectStopDto> findProjectStopByStopId(ODataObj oDataObj);

    ProjectStopDto getProjectStopByStopId(String stopId);

    void updateProjectStop(ProjectStopDto projectStopDto);

    List<ProjectStop> findPauseProjectSuccess();

    void updateProjectStopStatus(List<ProjectStop> projectStopList);

    int findMyPauseCount();

    /**
     * 获取主页面上的项目暂停申请信息
     * @return
     */
    List<ProjectStopDto> findHomeProjectStop();
}
