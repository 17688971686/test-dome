package cs.repository.repositoryImpl.flow;

import cs.domain.flow.RuProcessTask;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description:正在办理的任务
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
@Repository
public class RuProcessTaskRepoImpl extends AbstractRepository<RuProcessTask, String> implements RuProcessTaskRepo {
}