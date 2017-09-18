package cs.service.monthly;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.Constant;
import cs.common.Constant.EnumState;
import cs.common.Constant.MsgCode;
import cs.common.ResultMsg;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.monthly.MonthlyNewsletter;
import cs.domain.monthly.MonthlyNewsletter_;
import cs.domain.project.Sign;
import cs.model.PageModelDto;
import cs.model.monthly.MonthlyNewsletterDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.monthly.MonthlyNewsletterRepo;

/**
 * Description: 月报简报 业务操作实现类
 * author: sjy
 * Date: 2017-9-8 11:23:41
 */
@Service
public class MonthlyNewsletterServiceImpl  implements MonthlyNewsletterService {

	@Autowired
	private MonthlyNewsletterRepo monthlyNewsletterRepo;
	
	@Override
	public PageModelDto<MonthlyNewsletterDto> get(ODataObj odataObj) {
		PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
		List<MonthlyNewsletter> resultList = monthlyNewsletterRepo.findByOdata(odataObj);
		List<MonthlyNewsletterDto> resultDtoList = new ArrayList<MonthlyNewsletterDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				MonthlyNewsletterDto modelDto = new MonthlyNewsletterDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	/**
	 * 保存月报简报历史数据
	 * @return 
	 */
	@Override
	@Transactional
	public ResultMsg save(MonthlyNewsletterDto record) {
		MonthlyNewsletter domain = new MonthlyNewsletter(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setId(UUID.randomUUID().toString());
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setAddTime(now);
		domain.setAuthorizedUser(SessionUtil.getDisplayName());
		domain.setAuthorizedTime(now);
		domain.setMonthlyType(EnumState.NORMAL.getValue());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		monthlyNewsletterRepo.save(domain);
		return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", domain);
	}

	@Override
	@Transactional
	public void update(MonthlyNewsletterDto record) {
		MonthlyNewsletter domain = monthlyNewsletterRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());
		
		monthlyNewsletterRepo.save(domain);
	}

	@Override
	public MonthlyNewsletterDto findById(String id) {		
		MonthlyNewsletterDto modelDto = new MonthlyNewsletterDto();
		if(Validate.isString(id)){
			MonthlyNewsletter domain = monthlyNewsletterRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	/**
	 * 删除月报简报历史记录
	 */
	@Override
	@Transactional
	public void delete(String id) {
		MonthlyNewsletterDto monthlyDto = new MonthlyNewsletterDto();
		MonthlyNewsletter domain = monthlyNewsletterRepo.findById(id);
		domain.setMonthlyType(Constant.EnumState.DELETE.getValue());
		BeanCopierUtils.copyProperties(domain, monthlyDto);
		monthlyNewsletterRepo.save(domain);
	}

	@Override
	public void deletes(String[] ids) {
		for(String id :ids){
			this.delete(id);
		}
	}

	/**
	 * 删除月报简报历史列表
	 */
	@Override
	public PageModelDto<MonthlyNewsletterDto> deleteHistoryList(ODataObj odataObj) {
		PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
		Criteria criteria = monthlyNewsletterRepo.getExecutableCriteria();
		criteria = odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName(), EnumState.DELETE.getValue()));
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	    pageModelDto.setCount(totalResult);
	    criteria.setProjection(null);
	    if(odataObj.getSkip() > 0){
	    	criteria.setFirstResult(odataObj.getTop());
	    }
	    if(odataObj.getTop() > 0){
	    	criteria.setMaxResults(odataObj.getTop());
	    }
	    List<MonthlyNewsletter> monthlist =criteria.list();
		List<MonthlyNewsletterDto> monthDtos = new ArrayList<MonthlyNewsletterDto>(monthlist == null ? 0 : monthlist.size());
		
		if(monthlist != null && monthlist.size() > 0){
			monthlist.forEach(x->{
				MonthlyNewsletterDto monthDto = new MonthlyNewsletterDto();
				BeanCopierUtils.copyProperties(x, monthDto);
				monthDtos.add(monthDto);
			});						
		}		
		pageModelDto.setValue(monthDtos);	
		
		return pageModelDto;
	}

	/**
	 * 月报简报历史数据列表
	 */
	@Override
	public PageModelDto<MonthlyNewsletterDto> mothlyHistoryList(ODataObj odataObj) {
		PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
		Criteria criteria = monthlyNewsletterRepo.getExecutableCriteria();
		criteria = odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName(), EnumState.NORMAL.getValue()));
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	    pageModelDto.setCount(totalResult);
	    criteria.setProjection(null);
	    if(odataObj.getSkip() > 0){
	    	criteria.setFirstResult(odataObj.getTop());
	    }
	    if(odataObj.getTop() > 0){
	    	criteria.setMaxResults(odataObj.getTop());
	    }
	    List<MonthlyNewsletter> monthlist =criteria.list();
		List<MonthlyNewsletterDto> monthDtos = new ArrayList<MonthlyNewsletterDto>(monthlist == null ? 0 : monthlist.size());
		
		if(monthlist != null && monthlist.size() > 0){
			monthlist.forEach(x->{
				MonthlyNewsletterDto monthDto = new MonthlyNewsletterDto();
				BeanCopierUtils.copyProperties(x, monthDto);
				monthDtos.add(monthDto);
			});						
		}		
		pageModelDto.setValue(monthDtos);	
		
		return pageModelDto;
	}

	/**
	 * 保存月报简报
	 * @return 
	 */
	@Override
	public ResultMsg saveTheMonthly(MonthlyNewsletterDto record) {
		MonthlyNewsletter domain = new MonthlyNewsletter(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setId(UUID.randomUUID().toString());
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setAddTime(now);
		domain.setAuthorizedUser(SessionUtil.getDisplayName());
		domain.setAuthorizedTime(now);
		domain.setMonthlyNewsletterName(record.getReportMultiyear()+"月报简报数据");
		domain.setMonthlyType(EnumState.PROCESS.getValue());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		monthlyNewsletterRepo.save(domain);
		return new ResultMsg(true, MsgCode.OK.getValue(), "操作成功！", domain);
	}

	/**
	 * 获取月报简报管理数据列表
	 */
	@Override
	public PageModelDto<MonthlyNewsletterDto> getMonthlyList(ODataObj odataObj) {
		PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
		Criteria criteria = monthlyNewsletterRepo.getExecutableCriteria();
		criteria = odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName(), EnumState.PROCESS.getValue()));
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	    pageModelDto.setCount(totalResult);
	    criteria.setProjection(null);
	    if(odataObj.getSkip() > 0){
	    	criteria.setFirstResult(odataObj.getTop());
	    }
	    if(odataObj.getTop() > 0){
	    	criteria.setMaxResults(odataObj.getTop());
	    }
	    List<MonthlyNewsletter> monthlist =criteria.list();
		List<MonthlyNewsletterDto> monthDtos = new ArrayList<MonthlyNewsletterDto>(monthlist == null ? 0 : monthlist.size());
		
		if(monthlist != null && monthlist.size() > 0){
			monthlist.forEach(x->{
				MonthlyNewsletterDto monthDto = new MonthlyNewsletterDto();
				BeanCopierUtils.copyProperties(x, monthDto);
				monthDtos.add(monthDto);
			});						
		}		
		pageModelDto.setValue(monthDtos);	
		
		return pageModelDto;
	}

	/**
	 * 删除月报简报管理数据列表
	 */
	@Override
	public PageModelDto<MonthlyNewsletterDto> deleteMonthlyList(ODataObj odataObj) {
		PageModelDto<MonthlyNewsletterDto> pageModelDto = new PageModelDto<MonthlyNewsletterDto>();
		Criteria criteria = monthlyNewsletterRepo.getExecutableCriteria();
		criteria = odataObj.buildFilterToCriteria(criteria);
		criteria.add(Restrictions.eq(MonthlyNewsletter_.monthlyType.getName(), EnumState.STOP.getValue()));
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	    pageModelDto.setCount(totalResult);
	    criteria.setProjection(null);
	    if(odataObj.getSkip() > 0){
	    	criteria.setFirstResult(odataObj.getTop());
	    }
	    if(odataObj.getTop() > 0){
	    	criteria.setMaxResults(odataObj.getTop());
	    }
	    List<MonthlyNewsletter> monthlist =criteria.list();
		List<MonthlyNewsletterDto> monthDtos = new ArrayList<MonthlyNewsletterDto>(monthlist == null ? 0 : monthlist.size());
		
		if(monthlist != null && monthlist.size() > 0){
			monthlist.forEach(x->{
				MonthlyNewsletterDto monthDto = new MonthlyNewsletterDto();
				BeanCopierUtils.copyProperties(x, monthDto);
				monthDtos.add(monthDto);
			});						
		}		
		pageModelDto.setValue(monthDtos);	
		
		return pageModelDto;
	}

	@Override
	public void deleteMonthlyDatas(String[] ids) {
		for(String id :ids){
			this.delete(id);
		}
		
	}
	/**
	 * 删除月报简报记录
	 */
	@Override
	public void deleteMonthlyData(String id) {
		MonthlyNewsletterDto monthlyDto = new MonthlyNewsletterDto();
		MonthlyNewsletter domain = monthlyNewsletterRepo.findById(id);
		domain.setMonthlyType(Constant.EnumState.STOP.getValue());
		BeanCopierUtils.copyProperties(domain, monthlyDto);
		monthlyNewsletterRepo.save(domain);
		
	}
	
}