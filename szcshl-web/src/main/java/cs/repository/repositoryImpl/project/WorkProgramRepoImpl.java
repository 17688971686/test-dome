package cs.repository.repositoryImpl.project;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.domain.project.WorkProgram;
import cs.domain.project.WorkProgram_;
import cs.repository.AbstractRepository;

@Repository
public class WorkProgramRepoImpl extends AbstractRepository<WorkProgram,String> implements WorkProgramRepo {


}
