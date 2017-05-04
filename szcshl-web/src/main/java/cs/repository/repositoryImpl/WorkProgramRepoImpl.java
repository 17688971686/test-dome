package cs.repository.repositoryImpl;

import org.springframework.stereotype.Repository;

import cs.domain.WorkProgram;
import cs.repository.AbstractRepository;

@Repository
public class WorkProgramRepoImpl extends AbstractRepository<WorkProgram,String> implements WorkProgramRepo {

}
