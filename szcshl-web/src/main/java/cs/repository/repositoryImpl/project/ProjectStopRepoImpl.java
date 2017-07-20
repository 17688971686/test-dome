package cs.repository.repositoryImpl.project;

import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cs.common.HqlBuilder;
import cs.domain.flow.RuProcessTask_;
import cs.domain.project.ProjectStop;
import cs.domain.project.ProjectStop_;
import cs.domain.project.Sign_;
import cs.repository.AbstractRepository;

@Repository
public class ProjectStopRepoImpl extends AbstractRepository<ProjectStop, String> implements ProjectStopRepo {
	  @Autowired
	  private ProjectStopRepo projectStopRepo;
	  @Override
	  //获取已暂停项目
      public List<ProjectStop> getProjectStop(String signid,String ispause){
    	  HqlBuilder sqlBuilder = HqlBuilder.create();
    	  sqlBuilder.append("select p.* from cs_projectstop p where p."+Sign_.signid.getName()+" = '"+signid+"'");
    	  sqlBuilder.append(" and p."+ProjectStop_.ispause.getName()+" =:ispause");
    	  sqlBuilder.setParam("ispause",ispause);
    	  List<ProjectStop> pList=projectStopRepo.findBySql(sqlBuilder);
    	  return pList;
      }
}
