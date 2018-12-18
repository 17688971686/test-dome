package cs.repository.repositoryImpl.flow;

import cs.common.constants.Constant;
import cs.common.utils.SessionUtil;
import cs.domain.flow.RuProcessTask;
import cs.domain.flow.RuProcessTask_;
import cs.repository.AbstractRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        criteria.add(Restrictions.ne(RuProcessTask_.signState.getName(), Constant.EnumState.DELETE.getValue()));
        criteria.add(Restrictions.or(Restrictions.eq(RuProcessTask_.assignee.getName(), SessionUtil.getUserId()), Restrictions.like(RuProcessTask_.assigneeList.getName(), "%" + SessionUtil.getUserId() + "%") ));
        return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }
    /**
     * 根据processInstanceId实例ID查询在办项目数据
     * @param  processInstanceId
     */
    @Override

    public List<RuProcessTask> findRuProcessList(String processInstanceId){
        Criteria criteria= getExecutableCriteria();
        criteria.add(Restrictions.eq(RuProcessTask_.processInstanceId.getName() , processInstanceId));
        return criteria.list();
    }
}