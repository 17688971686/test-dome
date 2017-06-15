package cs.repository.repositoryImpl.project;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cs.common.HqlBuilder;
import cs.domain.project.AssistPlanSign;
import cs.domain.project.AssistPlanSign_;
import cs.domain.project.AssistPlan_;
import cs.model.project.AssistPlanSignDto;
import cs.repository.AbstractRepository;

/**
 * Description: 协审项目 数据操作实现类
 * author: ldm
 * Date: 2017-6-6 14:50:37
 */
@Repository
public class AssistPlanSignRepoImpl extends AbstractRepository<AssistPlanSign, String> implements AssistPlanSignRepo {

	@Override
	public List<AssistPlanSign> getAssistPlanSignByAssistPlanId(String assistPlanId) {
		
		Criteria criteria=this.getSession().createCriteria(AssistPlanSign.class);
		criteria.add(Restrictions.eq(AssistPlanSign_.assistPlan.getName()+"."+AssistPlan_.id.getName(), assistPlanId));
		return criteria.list();
		
//		HqlBuilder hqlBuilder=HqlBuilder.create();
//		hqlBuilder.append("select t.*,case when t.estimateCost is null then (select wp.appalyinvestment from CS_WORK_PROGRAM wp where wp.signid = t.signid) else t.estimateCost end estimateCost from CS_AS_PLANSIGN t ");
//		
//		
//		return null;
	}
}