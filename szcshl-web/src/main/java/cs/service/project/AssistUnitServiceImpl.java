package cs.service.project;

import cs.common.ICurrentUser;
import cs.common.service.ServiceImpl;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.project.AssistUnit;
import cs.domain.project.AssistUnit_;
import cs.model.PageModelDto;
import cs.model.project.AssistUnitDto;
import cs.repository.odata.ODataObj;

import cs.repository.repositoryImpl.project.AssistUnitRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 协审单位 业务操作实现类
 * author: ldm
 * Date: 2017-6-6 14:49:58
 */
@Service
public class AssistUnitServiceImpl  implements AssistUnitService {

	@Autowired
	private AssistUnitRepo assistUnitRepo;
	@Autowired
	private ICurrentUser currentUser;

	@Override
	public PageModelDto<AssistUnitDto> get(ODataObj odataObj) {
		PageModelDto<AssistUnitDto> pageModelDto = new PageModelDto<AssistUnitDto>();
		List<AssistUnit> resultList = assistUnitRepo.findByOdata(odataObj);
		List<AssistUnitDto> resultDtoList = new ArrayList<AssistUnitDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
			resultList.forEach(x->{
				AssistUnitDto modelDto = new AssistUnitDto();
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
	public void save(AssistUnitDto record) {
		AssistUnit domain = new AssistUnit(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(currentUser.getLoginName());
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		assistUnitRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(AssistUnitDto record) {
		AssistUnit domain = assistUnitRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setModifiedDate(new Date());
		
		assistUnitRepo.save(domain);
	}

	@Override
	public AssistUnitDto findById(String id) {		
		AssistUnitDto modelDto = new AssistUnitDto();
		if(Validate.isString(id)){
			AssistUnit domain = assistUnitRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {
		assistUnitRepo.deleteById(AssistUnit_.id.getName(),id);
	}
	
}