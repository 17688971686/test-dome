package cs.repository.repositoryImpl.project;

import cs.common.constants.Constant;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.project.ProjectStop;
import cs.domain.project.ProjectStop_;
import cs.domain.project.Sign_;
import cs.model.project.ProjectStopDto;
import cs.repository.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectStopRepoImpl extends AbstractRepository<ProjectStop, String> implements ProjectStopRepo {
    @Autowired
    private ProjectStopRepo projectStopRepo;

    /**
     * 根据项目和状态查询审批结果信息
     * @param signid        //项目ID
     * @param isactive      //是否审批通过
     * @param isactive      //是否已经执行完
     * @return
     */
    @Override
    public List<ProjectStop> findProjectStop(String signid,String isactive,String isOverTime) {
        HqlBuilder sqlBuilder = HqlBuilder.create();
        sqlBuilder.append("select p.* from cs_projectstop p where p." + Sign_.signid.getName() + " = :signid ");
        sqlBuilder.setParam("signid",signid);

        if(Validate.isString(isactive)){
            sqlBuilder.append(" and p." + ProjectStop_.isactive.getName() + " =:isactive");
            sqlBuilder.setParam("isactive", isactive);
        }
        if(Validate.isString(isOverTime)){
            sqlBuilder.append(" and p." + ProjectStop_.isOverTime.getName() + " =:isOverTime");
            sqlBuilder.setParam("isOverTime", isOverTime);
        }
        List<ProjectStop> pList = projectStopRepo.findBySql(sqlBuilder);
        return pList;
    }

    /**
     * 通过收文ID获取审批通过的项目暂停信息
     * @param signId
     * @return
     */
    @Override
    public List<ProjectStop> getStopList(String signId) {
        HqlBuilder hqlBuilder = HqlBuilder.create();
        hqlBuilder.append(" from " + ProjectStop.class.getSimpleName());
        hqlBuilder.append(" where " + ProjectStop_.sign.getName() + "." + Sign_.signid.getName() + "=:signId" );
        hqlBuilder.append(" and " + ProjectStop_.isactive.getName() + "=:isActive");
        hqlBuilder.setParam("signId" , signId);
        hqlBuilder.setParam("isActive" , Constant.EnumState.YES.getValue());
        return this.findByHql(hqlBuilder);

    }

}
