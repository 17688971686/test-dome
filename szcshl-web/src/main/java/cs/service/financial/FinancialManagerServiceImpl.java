package cs.service.financial;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.impl.bpmn.data.Data;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.HqlBuilder;
import cs.common.Constant.EnumState;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.financial.FinancialManager;
import cs.domain.financial.FinancialManager_;
import cs.domain.project.AssistPlanSign;
import cs.domain.project.AssistPlanSign_;
import cs.domain.project.Sign;
import cs.domain.project.Sign_;
import cs.domain.project.WorkProgram_;
import cs.model.PageModelDto;
import cs.model.financial.FinancialManagerDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.financial.FinancialManagerRepo;
import cs.repository.repositoryImpl.project.SignRepo;
/**
 * Description: 财务管理 业务操作实现类
 * author: sjy
 * Date: 2017-8-7 11:32:03
 */
@Service
public class FinancialManagerServiceImpl  implements FinancialManagerService {

	private static Logger log = Logger.getLogger(FinancialManagerServiceImpl.class);
	@Autowired
	private FinancialManagerRepo financialManagerRepo;
	@Autowired
	private SignRepo signRepo;
	
	@Override
	public PageModelDto<SignDto> get(ODataObj odataObj) {
		PageModelDto<SignDto> pageModelDto = new PageModelDto<SignDto>();
		Criteria criteria = signRepo.getExecutableCriteria();
		criteria =odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(Sign_.financiaStatus.getName(),EnumState.YES.getValue()));
	    Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	    pageModelDto.setCount(totalResult);
	    criteria.setProjection(null);
	    if(odataObj.getSkip() > 0){
	    	criteria.setFirstResult(odataObj.getTop());
	    }
	    if(odataObj.getTop() > 0){
	    	criteria.setMaxResults(odataObj.getTop());
	    }
	    List<Sign> signlist =criteria.list();
		List<SignDto> signDtos = new ArrayList<SignDto>(signlist == null ? 0 : signlist.size());
		
		if(signlist != null && signlist.size() > 0){
			signlist.forEach(x->{
				 SignDto signDto = new SignDto();
				BeanCopierUtils.copyProperties(x, signDto);
				
				signDtos.add(signDto);
			});						
		}		
		pageModelDto.setValue(signDtos);	
		
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(FinancialManagerDto record) {
		FinancialManager domain = new FinancialManager(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		
		if(record.getId() == null){
			domain.setId(UUID.randomUUID().toString());
		}
		domain.setCreatedBy(SessionUtil.getLoginName());
		domain.setModifiedBy(SessionUtil.getLoginName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		financialManagerRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(FinancialManagerDto record) {
		FinancialManager domain = financialManagerRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		//domain.setModifiedBy(currentUser.getLoginName());
		domain.setModifiedDate(new Date());
		
		financialManagerRepo.save(domain);
	}

	@Override
	public FinancialManagerDto findById(String id) {		
		FinancialManagerDto modelDto = new FinancialManagerDto();
		if(Validate.isString(id)){
			FinancialManager domain = financialManagerRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {
		List<String> idFinancial = StringUtil.getSplit(id, ",");
		if(idFinancial !=null && idFinancial.size() > 0){
			for( int i = 0 ;i < idFinancial.size() ;i++){
				financialManagerRepo.deleteById(FinancialManager_.id.getName(), idFinancial.get(i));
			}
		}
	}


	@Override
	public Integer sunCount(String businessId) {
		HqlBuilder hql = HqlBuilder.create();
		hql.append(" select sum(charge) from CS_FINANCIAL_MANAGER ");
		hql.append(" where " +FinancialManager_.businessId.getName()+" =:businessId");
	    hql.setParam("businessId", businessId);
		
		return financialManagerRepo.returnIntBySql(hql);
	}

	@Override
	public Map<String, Object> initfinancialData(String businessId) {
		Map<String, Object> map = new HashMap<String, Object>();
		 Sign sign = signRepo.findById(businessId);
		 FinancialManagerDto financialDto = new FinancialManagerDto();
		 financialDto.setProjectName(sign.getProjectname());
		 financialDto.setSignid(sign.getSignid());
		 financialDto.setPaymentData(new Date());
		 
		 financialDto.setAssissCost(sign.getDeclaration());//计划协审费用
		 financialDto.setAssistBuiltcompanyName(sign.getBuiltcompanyName());//协审单位
		 
		 HqlBuilder hqlBuilder = HqlBuilder.create();
		 hqlBuilder.append(" from "+FinancialManager.class.getSimpleName() + " where "+FinancialManager_.businessId.getName()+ " =:businessId");
		 hqlBuilder.setParam("businessId", businessId);
		 List<FinancialManager> financiallist= financialManagerRepo.findByHql(hqlBuilder);
		
		 map.put("financiallist", financiallist);
		 map.put("financialDto", financialDto);
		return map;
	}

	/**
	 * 获取协审费用统计列表数据
	 */
	@Override
	public PageModelDto<SignDto> assistCostCountGet(ODataObj odataObj) {
		PageModelDto<SignDto> pageModelDto = new PageModelDto<SignDto>();
		Criteria criteria = signRepo.getExecutableCriteria();
		criteria =odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(Sign_.assistStatus.getName(),EnumState.YES.getValue()));
	    Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	    pageModelDto.setCount(totalResult);
	    criteria.setProjection(null);
	    if(odataObj.getSkip() > 0){
	    	criteria.setFirstResult(odataObj.getTop());
	    }
	    if(odataObj.getTop() > 0){
	    	criteria.setMaxResults(odataObj.getTop());
	    }
	    List<Sign> signlist =criteria.list();
		List<SignDto> signDtos = new ArrayList<SignDto>(signlist == null ? 0 : signlist.size());
		
		if(signlist != null && signlist.size() > 0){
			signlist.forEach(x->{
				 SignDto signDto = new SignDto();
				BeanCopierUtils.copyProperties(x, signDto);
				
				signDtos.add(signDto);
			});						
		}		
		pageModelDto.setValue(signDtos);	
		
		return pageModelDto;
	}
	
}