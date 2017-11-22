package cs.repository.repositoryImpl.project;

import java.math.BigDecimal;
import java.util.List;

import cs.common.Constant;
import cs.common.ResultMsg;
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

	/**
	 * 通过收文Id 获取协审单位和协审费用
	 * @param signId
	 * @return
	 */
	@Override
	public ResultMsg findAssistPlanSignById(String signId) {
		AssistPlanSignDto assistPlanSignDto = new AssistPlanSignDto();
		HqlBuilder hqlBuilder = HqlBuilder.create();
		hqlBuilder.append("select sum(p.assistCost) numCost , wm_concat(c.unitName) unitNameStr from cs_as_plansign p ");
		hqlBuilder.append(" left join cs_as_unit c on p.assistUnitId = c.id ");
		hqlBuilder.append(" where p.signId =:signId").setParam("signId" , signId);
		List<Object[]> assistList = this.getObjectArray(hqlBuilder);
		if(assistList != null && assistList.size()>0){
			Object[] objects = assistList.get(0);
			BigDecimal numCost = objects[0] == null ? new BigDecimal(0) : new BigDecimal(objects[0].toString());
			String unitName = objects[1] == null ? "" : objects[1].toString();
			assistPlanSignDto.setAssistCost(numCost);
			assistPlanSignDto.setUnitNameStr(unitName);
		}
		return new ResultMsg(true , Constant.MsgCode.OK.getValue() , "查询成功！" , assistPlanSignDto);
	}
}