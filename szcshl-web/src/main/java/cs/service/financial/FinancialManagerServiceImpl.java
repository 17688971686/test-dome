package cs.service.financial;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.HqlBuilder;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.StringUtil;
import cs.common.utils.Validate;
import cs.domain.financial.FinancialManager;
import cs.domain.financial.FinancialManager_;
import cs.domain.project.AssistPlanSign;
import cs.domain.project.AssistPlanSign_;
import cs.domain.project.Sign;
import cs.domain.project.WorkProgram_;
import cs.model.PageModelDto;
import cs.model.financial.FinancialManagerDto;
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
	public PageModelDto<FinancialManagerDto> get(ODataObj odataObj) {
		PageModelDto<FinancialManagerDto> pageModelDto = new PageModelDto<FinancialManagerDto>();
		List<FinancialManager> resultList = financialManagerRepo.findByOdata(odataObj);
		List<FinancialManagerDto> resultDtoList = new ArrayList<FinancialManagerDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				FinancialManager modelDto = new FinancialManager();
				BeanCopierUtils.copyProperties(x, modelDto);
				//cannot copy 
				modelDto.setCreatedDate(x.getCreatedDate());
				modelDto.setModifiedDate(x.getModifiedDate());
				
				//resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
	//	pageModelDto.setValue(FinancialManagerDtoList);		
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
	public Integer sunCount(String signid) {
		HqlBuilder hql = HqlBuilder.create();
		hql.append(" select sum(charge) from CS_FINANCIAL_MANAGER ");
		hql.append(" where " +FinancialManager_.signid.getName()+" =:signid");
	    hql.setParam("signid", signid);
		
		return financialManagerRepo.returnIntBySql(hql);
	}

	@Override
	public Map<String, Object> initfinancialData(String signid) {
		Map<String, Object> map = new HashMap<String, Object>();
		 Sign sign = signRepo.findById(signid);
		 FinancialManagerDto financialDto = new FinancialManagerDto();
		 financialDto.setProjectName(sign.getProjectname());
		 financialDto.setSignid(sign.getSignid());
		 HqlBuilder hqlBuilder = HqlBuilder.create();
		 hqlBuilder.append(" from "+FinancialManager.class.getSimpleName() + " where "+FinancialManager_.signid.getName()+ " =:signid");
		 hqlBuilder.setParam("signid", signid);
		 List<FinancialManager> financiallist= financialManagerRepo.findByHql(hqlBuilder);
		 map.put("financiallist", financiallist);
		 map.put("financialDto", financialDto);
		return map;
	}
	
}