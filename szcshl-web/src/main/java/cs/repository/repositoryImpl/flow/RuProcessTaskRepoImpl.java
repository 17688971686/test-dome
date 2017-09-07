package cs.repository.repositoryImpl.flow;

import cs.common.utils.SessionUtil;
import cs.domain.flow.RuProcessTask;
import cs.domain.flow.RuProcessTask_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Description:正在办理的任务
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
@Repository
public class RuProcessTaskRepoImpl extends AbstractRepository<RuProcessTask, String> implements RuProcessTaskRepo {
    /**
     * 查询个人待办项目总数
     * @return
     */
    @Override
    public int findMyDoingTask() {
        Criteria criteria = getExecutableCriteria();
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(RuProcessTask_.assignee.getName(), SessionUtil.getUserId()));
        dis.add(Restrictions.like(RuProcessTask_.assigneeList.getName(), "%" + SessionUtil.getUserId() + "%"));
        criteria.add(dis);
        return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }
}