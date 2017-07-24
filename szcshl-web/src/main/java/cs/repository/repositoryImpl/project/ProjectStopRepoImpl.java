package cs.repository.repositoryImpl.project;

import cs.common.Constant;
import cs.common.HqlBuilder;
import cs.domain.project.ProjectStop;
import cs.domain.project.ProjectStop_;
import cs.domain.project.Sign_;
import cs.repository.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectStopRepoImpl extends AbstractRepository<ProjectStop, String> implements ProjectStopRepo {
    @Autowired
    private ProjectStopRepo projectStopRepo;

    @Override
    //获取已暂停项目
    public List<ProjectStop> getProjectStop(String signid, String ispause) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select p.* from cs_projectstop p where p." + Sign_.signid.getName() + " = '" + signid + "'");
        sqlBuilder.append(" and p." + ProjectStop_.isactive.getName() + " =:isactive");
        sqlBuilder.setParam("isactive", ispause);
        List<ProjectStop> pList = projectStopRepo.findBySql(sqlBuilder);
        return pList;
    }

    @Override
    public ProjectStop findProjectStop(String signid) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + ProjectStop.class.getSimpleName() + " where " + ProjectStop_.sign.getName() + "." + Sign_.signid.getName());
        hqlBuilder.append(" =:signid ").setParam("signid", signid);
        hqlBuilder.append(" and " + ProjectStop_.isactive.getName() + " =:isactive").setParam("isactive", Constant.EnumState.YES.getValue());
        hqlBuilder.append(" order by "+ProjectStop_.createdDate.getName()+" desc");
        List<ProjectStop> resultList = projectStopRepo.findByHql(hqlBuilder);
        if(resultList != null && resultList.size() > 0){
            return resultList.get(0);
        }
        return null;
    }
}
