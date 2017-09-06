package cs.repository.repositoryImpl.topic;

import cs.domain.topic.WorkPlan;
import cs.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

/**
 * Description:  数据操作实现类
 * author: ldm
 * Date: 2017-9-4 15:33:03
 */
@Repository
public class WorkPlanRepoImpl extends AbstractRepository<WorkPlan, String> implements WorkPlanRepo {
}