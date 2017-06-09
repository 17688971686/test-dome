package cs.service.project;

import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.project.AssistUnitUser;
import cs.model.PageModelDto;
import cs.model.project.AssistUnitUserDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.project.AssistUnitUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 协审单位用户 业务操作实现类
 * author: ldm
 * Date: 2017-6-9 9:37:54
 */
@Service
public class AssistUnitUserServiceImpl  implements AssistUnitUserService {

	@Autowired
	private AssistUnitUserRepo assistUnitUserRepo;
	@Autowired
	private ICurrentUser currentUser;
	
	@Override
	public PageModelDto<AssistUnitUserDto> get(ODataObj odataObj) {
		PageModelDto<AssistUnitUserDto> pageModelDto = new PageModelDto<AssistUnitUserDto>();
		List<AssistUnitUser> resultList = assistUnitUserRepo.findByOdata(odataObj);
		List<AssistUnitUserDto> resultDtoList = new ArrayList<AssistUnitUserDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultDtoList.forEach(x->{
				AssistUnitUserDto modelDto = new AssistUnitUserDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				//cannot copy 
				modelDto.setCreatedDate(x.getCreatedDate());
				modelDto.setModifiedDate(x.getModifiedDate());
				
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		pageModelDto.setValue(resultDtoList);
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(AssistUnitUserDto record) {
		AssistUnitUser domain = new AssistUnitUser(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(currentUser.getLoginName());
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		assistUnitUserRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(AssistUnitUserDto record) {
		AssistUnitUser domain = assistUnitUserRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setModifiedDate(new Date());
		
		assistUnitUserRepo.save(domain);
	}

	@Override
	public AssistUnitUserDto findById(String id) {		
		AssistUnitUserDto modelDto = new AssistUnitUserDto();
		if(Validate.isString(id)){
			AssistUnitUser domain = assistUnitUserRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {

	}
	
}