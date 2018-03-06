package cs.mobile.service;

import cs.common.Constant;
import cs.common.utils.SessionUtil;
import cs.domain.flow.RuProcessTask;
import cs.domain.flow.RuProcessTask_;
import cs.domain.flow.RuTask;
import cs.domain.flow.RuTask_;
import cs.model.PageModelDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.flow.RuProcessTaskRepo;
import cs.repository.repositoryImpl.flow.RuTaskRepo;
import cs.service.project.SignService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */
@Service
public class WorkDynamicServiceImpl implements WorkDynamicService {
    @Autowired
    private RuTaskRepo ruTaskRepo;
    @Autowired
    private RuProcessTaskRepo ruProcessTaskRepo;
    @Autowired
    private SignService signService;

    /**
     * 查询个人待办项目总数
     * @return
     */
    @Override
    public int findMyDoingSignTask( String id) {
        Criteria criteria =ruProcessTaskRepo.getExecutableCriteria();
        criteria.add(Restrictions.ne(RuProcessTask_.signState.getName(), Constant.EnumState.DELETE.getValue()));
        criteria.add(Restrictions.or(Restrictions.eq(RuProcessTask_.assignee.getName(), id), Restrictions.like(RuProcessTask_.assigneeList.getName(), "%" + id + "%") ));
        return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    /**
     * 查询个人待办任务总数
     * @return
     */
    @Override
    public int findMyDoingTask( String id) {
        Criteria criteria = ruTaskRepo.getExecutableCriteria();
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(RuTask_.assignee.getName(), id));
        dis.add(Restrictions.like(RuTask_.assigneeList.getName(), "%" + id + "%"));
        criteria.add(dis);
        return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    /**
     * 在办任务查询
     *
     * @param isUserDeal 是否为个人待办
     * @return
     */
    @Override
    public PageModelDto<RuProcessTask> queryRunProcessTasks(String  id, boolean isUserDeal) {
        PageModelDto<RuProcessTask> pageModelDto = new PageModelDto<RuProcessTask>();
        Criteria criteria = ruProcessTaskRepo.getExecutableCriteria();
        if (isUserDeal) {
            criteria.add(Restrictions.or(Restrictions.eq(RuProcessTask_.assignee.getName(), id),Restrictions.like(RuProcessTask_.assigneeList.getName(), "%" + id + "%")));
        }
        criteria.add(Restrictions.ne(RuProcessTask_.signState.getName(), Constant.EnumState.DELETE.getValue()));
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);
        List<RuProcessTask> runProcessList = criteria.list();
        //合并评审项目处理
        runProcessList.forEach(rl -> {
            //如果是合并评审主项目，则查询次项目，如果是合并评审次项目，则查询主项目
            if (Constant.EnumState.YES.getValue().equals(rl.getReviewType())) {
                rl.setReviewSignDtoList(signService.findReviewSign(rl.getBusinessKey()));
            }else if (Constant.EnumState.NO.getValue().equals(rl.getReviewType())) {
                rl.setReviewSignDtoList(signService.findMainReviewSign(rl.getBusinessKey()));
            }
        });

        pageModelDto.setCount(totalResult);
        pageModelDto.setValue(runProcessList);
        return pageModelDto;
    }


    /**
     * 查询我的待办任务（除项目签收流程外）
     *
     * @return
     */
    @Override
    public PageModelDto<RuTask> queryMyAgendaTask(String  id) {
        PageModelDto<RuTask> pageModelDto = new PageModelDto<RuTask>();
        Criteria criteria = ruTaskRepo.getExecutableCriteria();
        Disjunction dis = Restrictions.disjunction();
        dis.add(Restrictions.eq(RuTask_.assignee.getName(), id));
        dis.add(Restrictions.like(RuTask_.assigneeList.getName(), "%" + id + "%"));
        criteria.add(dis);
        Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(null);
        List<RuTask> runProcessList = criteria.list();
        pageModelDto.setCount(totalResult);
        pageModelDto.setValue(runProcessList);
        return pageModelDto;
    }


}
