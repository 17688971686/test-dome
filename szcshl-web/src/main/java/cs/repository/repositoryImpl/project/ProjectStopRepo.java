package cs.repository.repositoryImpl.project;

import java.util.List;

import cs.domain.project.ProjectStop;
import cs.model.project.ProjectStopDto;
import cs.repository.IRepository;

public interface ProjectStopRepo extends IRepository<ProjectStop, String> {
    List<ProjectStop> getProjectStop(String signid, String ispause);

    ProjectStop findProjectStop(String signid);

    /**
     * 通过收文ID获取审批通过的项目暂停信息
     * @param signId
     * @return
     */
    List<ProjectStopDto> getStopList(String signId);
}
