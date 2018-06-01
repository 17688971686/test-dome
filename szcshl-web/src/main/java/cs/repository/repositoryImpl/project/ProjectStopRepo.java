package cs.repository.repositoryImpl.project;

import java.util.List;

import cs.common.HqlBuilder;
import cs.domain.project.ProjectStop;
import cs.model.project.ProjectStopDto;
import cs.repository.IRepository;

public interface ProjectStopRepo extends IRepository<ProjectStop, String> {
    /**
     * 根据项目和状态查询审批结果信息
     *
     * @param signid   //项目ID
     * @param isactive //是否审批通过
     * @param isactive //是否已经执行完
     * @return
     */
    List<ProjectStop> findProjectStop(String signid, String isactive, String isOverTime);

    /**
     * 通过收文ID获取审批通过的项目暂停信息
     *
     * @param signId
     * @return
     */
    List<ProjectStop> getStopList(String signId);

}
