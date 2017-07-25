package cs.service.sharing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.sharing.SharingPrivilege;
import cs.model.PageModelDto;
import cs.model.sharing.SharingPrivilegeDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.sharing.SharingPrivilegeRepo;

/**
 * Description: 共享平台 业务操作实现类
 * author: sjy
 * Date: 2017-7-20 18:23:08
 */
@Service
public class SharingPrivilegeServiceImpl  implements SharingPrivilegeService {

	@Autowired
	private SharingPrivilegeRepo sharingPrivilegeRepo;
	@Autowired
	private ICurrentUser currentUser;
	
	@Override
	public PageModelDto<SharingPrivilegeDto> get(ODataObj odataObj) {
		PageModelDto<SharingPrivilegeDto> pageModelDto = new PageModelDto<SharingPrivilegeDto>();
		List<SharingPrivilege> resultList = sharingPrivilegeRepo.findByOdata(odataObj);
		List<SharingPrivilegeDto> resultDtoList = new ArrayList<SharingPrivilegeDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				SharingPrivilegeDto modelDto = new SharingPrivilegeDto();
				BeanCopierUtils.copyProperties(x, modelDto);
				//cannot copy 
				modelDto.setCreatedDate(x.getCreatedDate());
				modelDto.setModifiedDate(x.getModifiedDate());
				
				resultDtoList.add(modelDto);
			});						
		}		
		pageModelDto.setCount(odataObj.getCount());
		//pageModelDto.setValue(SharingPrivilegeDtoList);		
		return pageModelDto;
	}

	@Override
	@Transactional
	public void save(SharingPrivilegeDto record) {
		SharingPrivilege domain = new SharingPrivilege(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(currentUser.getLoginName());
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		sharingPrivilegeRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(SharingPrivilegeDto record) {
		SharingPrivilege domain = sharingPrivilegeRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(currentUser.getLoginName());
		domain.setModifiedDate(new Date());
		
		sharingPrivilegeRepo.save(domain);
	}

	@Override
	public SharingPrivilegeDto findById(String id) {		
		SharingPrivilegeDto modelDto = new SharingPrivilegeDto();
		if(Validate.isString(id)){
			SharingPrivilege domain = sharingPrivilegeRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {

	}
	
}