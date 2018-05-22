package cs.repository.repositoryImpl.project;

import cs.domain.project.AgentTask;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by ldm on 2018/5/21.
 */
@Repository
public class AgentTaskRepoImpl extends AbstractRepository<AgentTask, String> implements AgentTaskRepo {
}
