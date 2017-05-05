package cs.repository.repositoryImpl.project;

import org.springframework.stereotype.Repository;

import cs.domain.project.WorkProgram;
import cs.repository.AbstractRepository;

@Repository
public class WorkProgramRepoImpl extends AbstractRepository<WorkProgram,String> implements WorkProgramRepo {

}
