package cs.service.topic;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.topic.WorkPlan;
import cs.model.PageModelDto;
import cs.model.topic.WorkPlanDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.topic.WorkPlanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:  业务操作实现类
 * author: ldm
 * Date: 2017-9-4 15:33:03
 */
@Service
public class WorkPlanServiceImpl  implements WorkPlanService {

	@Autowired
	private WorkPlanRepo workPlanRepo;
	
	@Override
	public PageModelDto<WorkPlanDto> get(ODataObj odataObj) {
		PageModelDto<WorkPlanDto> pageModelDto = new PageModelDto<WorkPlanDto>();
		List<WorkPlan> resultList = workPlanRepo.findByOdata(odataObj);
		List<WorkPlanDto> resultDtoList = new ArrayList<WorkPlanDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				WorkPlanDto modelDto = new WorkPlanDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(WorkPlanDto record) {
		WorkPlan domain = new WorkPlan(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		workPlanRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(WorkPlanDto record) {
		WorkPlan domain = workPlanRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());
		
		workPlanRepo.save(domain);
	}

	@Override
	public WorkPlanDto findById(String id) {		
		WorkPlanDto modelDto = new WorkPlanDto();
		if(Validate.isString(id)){
			WorkPlan domain = workPlanRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {

	}
	
}