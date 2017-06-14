package cs.service.project;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.domain.project.AssistPlan;
import cs.domain.project.AssistPlanSign;
import cs.domain.project.AssistPlanSign_;
import cs.domain.project.WorkProgram;
import cs.model.project.AssistPlanSignDto;
import cs.repository.repositoryImpl.project.AssistPlanRepo;
import cs.repository.repositoryImpl.project.AssistPlanSignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;

/**
 * Description: 协审项目 业务操作实现类
 * author: ldm
 * Date: 2017-6-6 14:50:37
 */
@Service
public class AssistPlanSignServiceImpl  implements AssistPlanSignService {

	@Autowired
	private AssistPlanSignRepo assistPlanSignRepo;

	@Autowired
	private AssistPlanRepo assistPlanRepo;
	
	@Autowired
	private WorkProgramRepo workProgramRepo;
	@Autowired
	private ICurrentUser currentUser;
	
	@Override
	@Transactional
	public List<AssistPlanSignDto> getPlanSignByPlanId(String planId) {

		AssistPlanSignDto planSignDto=new AssistPlanSignDto();
		HqlBuilder hqlBuilder=HqlBuilder.create();
		hqlBuilder.append("select id,signId,projectName,assistType,mainSignId,assistCost,jiananCost,assistDays,isMain,splitNum,assistUnitId,planId,case when t.estimateCost is null then (");
		hqlBuilder.append("select wp.appalyinvestment from CS_WORK_PROGRAM wp where wp.signid = t.signid)");
		hqlBuilder.append("else t.estimateCost end estimateCost from CS_AS_PLANSIGN t where t.planId=:planId");
		hqlBuilder.setParam("planId", planId);
		
		List<AssistPlanSign> list=assistPlanSignRepo.findBySql(hqlBuilder);
		List<AssistPlanSignDto> dtoList=new ArrayList<>();
		for(AssistPlanSign assistPlanSign:list){
			AssistPlanSignDto assistPlanSignDto=new AssistPlanSignDto();
			BeanCopierUtils.copyProperties(assistPlanSign, assistPlanSignDto);
			assistPlanSignDto.setPlanId(planId);
			dtoList.add(assistPlanSignDto);
		}
		return dtoList;
	}
	
	
	@Override
	@Transactional
	public void savePlanSign(AssistPlanSignDto[] planSigns) {

		for(AssistPlanSignDto planSignDto:planSigns){
			AssistPlan assistPlan=assistPlanRepo.findById(planSignDto.getPlanId());
			AssistPlanSign planSign=new AssistPlanSign();
			BeanCopierUtils.copyProperties(planSignDto, planSign);
			planSign.setAssistPlan(assistPlan);
			assistPlanSignRepo.save(planSign);
		}

	}
	@Override
	public List<AssistPlanSignDto> findBySignId(String signId) {
		Criteria criteria = assistPlanSignRepo.getExecutableCriteria();
		criteria.add(Restrictions.eq(AssistPlanSign_.signId.getName(),signId));
		List<AssistPlanSign> list = criteria.list();
		List<AssistPlanSignDto> resultList = new ArrayList<>(list == null?0:list.size());
		if(list != null && list.size() > 0){
			list.forEach( l -> {
				AssistPlanSignDto assistPlanSignDto = new AssistPlanSignDto();
				BeanCopierUtils.copyProperties(l,assistPlanSignDto);
				resultList.add(assistPlanSignDto);
			});
		}
		return resultList;
	}
}