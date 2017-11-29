package cs.service.project;

import java.util.ArrayList;
import java.util.List;

import cs.common.Constant;
import cs.common.ResultMsg;
import cs.common.utils.Validate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.Constant.EnumState;
import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.domain.project.AssistPlan;
import cs.domain.project.AssistPlanSign;
import cs.domain.project.AssistPlanSign_;
import cs.domain.sys.User;
import cs.model.project.AssistPlanSignDto;
import cs.repository.repositoryImpl.project.AssistPlanRepo;
import cs.repository.repositoryImpl.project.AssistPlanSignRepo;
import cs.repository.repositoryImpl.project.SignRepo;
import cs.repository.repositoryImpl.project.WorkProgramRepo;
import cs.repository.repositoryImpl.sys.UserRepo;

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
    private SignPrincipalService signPrincipalService;
	
	@Override
	@Transactional
	public List<AssistPlanSignDto> getPlanSignByPlanId(String planId) {
		HqlBuilder hqlBuilder=HqlBuilder.create();
		hqlBuilder.append("select id,signId,projectName,assistType,mainSignId,assistCost,jiananCost,assistDays,isMain,splitNum,assistUnitId,planId,case when t.estimateCost is null then (");
		hqlBuilder.append("select wp.declaration from CS_WORK_PROGRAM wp where wp.signid = t.signid and wp.branchid='1')");
		hqlBuilder.append("else t.estimateCost end estimateCost from CS_AS_PLANSIGN t where t.planId=:planId");
		hqlBuilder.setParam("planId", planId);
		List<AssistPlanSign> list=assistPlanSignRepo.findBySql(hqlBuilder);
		List<AssistPlanSignDto> dtoList=new ArrayList<>();
		for(AssistPlanSign assistPlanSign:list){
			AssistPlanSignDto assistPlanSignDto=new AssistPlanSignDto();
			BeanCopierUtils.copyProperties(assistPlanSign, assistPlanSignDto);
			User user = signPrincipalService.getMainPriUser(assistPlanSign.getSignId());
			assistPlanSignDto.setUserName(user == null?"":user.getDisplayName());
			assistPlanSignDto.setPlanId(planId);
			dtoList.add(assistPlanSignDto);
		}
		return dtoList;
	}

	/**
	 * 保存协审计划信息
	 * @param planSigns
	 * @return
	 */
	@Override
	@Transactional
	public ResultMsg savePlanSign(AssistPlanSignDto[] planSigns) {
	    try{
            for(AssistPlanSignDto planSignDto:planSigns){
                /*AssistPlan assistPlan = assistPlanRepo.findById(planSignDto.getPlanId());
                AssistPlanSign planSign = new AssistPlanSign();
                BeanCopierUtils.copyProperties(planSignDto, planSign);
                planSign.setAssistPlan(assistPlan);
                assistPlanSignRepo.save(planSign);*/

                AssistPlanSign planSign = assistPlanSignRepo.findById(AssistPlanSign_.id.getName(),planSignDto.getId());
                BeanCopierUtils.copyProperties(planSignDto, planSign);
                assistPlanSignRepo.save(planSign);
            }
            return new ResultMsg(true, Constant.MsgCode.OK.getValue(),"操作成功！");
        }catch(Exception e){
	        return new ResultMsg(false, Constant.MsgCode.ERROR.getValue(),"操作失败:"+e.getMessage());
        }
	}

	/**
	 * 根据项目ID，查询对应的项目信息
	 * @param signId
	 * @return
	 */
	@Override
	public List<AssistPlanSignDto> findBySignId(String signId) {
		Criteria criteria = assistPlanSignRepo.getExecutableCriteria();
		criteria.add(Restrictions.eq(AssistPlanSign_.signId.getName(),signId));
        criteria.addOrder(Order.desc(AssistPlanSign_.isMain.getName())).addOrder(Order.asc(AssistPlanSign_.splitNum.getName()));
		List<AssistPlanSign> list = criteria.list();
		List<AssistPlanSignDto> resultList = new ArrayList<>(list == null?0:list.size());
		if(Validate.isList(list)){
			list.forEach( l -> {
				AssistPlanSignDto assistPlanSignDto = new AssistPlanSignDto();
				BeanCopierUtils.copyProperties(l,assistPlanSignDto);
				resultList.add(assistPlanSignDto);
			});
		}
		return resultList;
	}

	/**
	 * 通过收文Id 获取协审单位和协审费用
	 * @param signId
	 * @return
	 */
	@Override
	public ResultMsg findAssistPlanSignBySignId(String signId) {
		return assistPlanSignRepo.findAssistPlanSignById(signId);
	}
}