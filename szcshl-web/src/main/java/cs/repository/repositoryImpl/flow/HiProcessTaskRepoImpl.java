package cs.repository.repositoryImpl.flow;

import cs.domain.flow.HiProcessTask;
import cs.domain.flow.RuProcessTask;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description:已办理任务信息
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
@Repository
public class HiProcessTaskRepoImpl extends AbstractRepository<HiProcessTask, String> implements HiProcessTaskRepo {
}