package cs.repository.repositoryImpl.flow;

import cs.domain.flow.RuProcessTask;
import cs.repository.IRepository;

import java.util.List;

/**
 * Description: 正在办理的项目
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
public interface RuProcessTaskRepo extends IRepository<RuProcessTask, String> {

    int findMyDoingTask();

    List<RuProcessTask> findRuProcessList(String processInstanceId);
}
