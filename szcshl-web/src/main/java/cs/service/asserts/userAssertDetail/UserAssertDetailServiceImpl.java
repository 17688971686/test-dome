package cs.service.asserts.userAssertDetail;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.asserts.userAssertDetail.UserAssertDetail;
import cs.model.PageModelDto;
import cs.model.asserts.userAssertDetail.UserAssertDetailDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.asserts.userAssertDetail.UserAssertDetailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 用户资产明细 业务操作实现类
 * author: zsl
 * Date: 2017-9-20 15:35:02
 */
@Service
public class UserAssertDetailServiceImpl  implements UserAssertDetailService {

	@Autowired
	private UserAssertDetailRepo userAssertDetailRepo;
	
	@Override
	public PageModelDto<UserAssertDetailDto> get(ODataObj odataObj) {
		PageModelDto<UserAssertDetailDto> pageModelDto = new PageModelDto<UserAssertDetailDto>();
		List<UserAssertDetail> resultList = userAssertDetailRepo.findByOdata(odataObj);
		List<UserAssertDetailDto> resultDtoList = new ArrayList<UserAssertDetailDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				UserAssertDetailDto modelDto = new UserAssertDetailDto();
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
	public void save(UserAssertDetailDto record) {
		UserAssertDetail domain = new UserAssertDetail(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		userAssertDetailRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(UserAssertDetailDto record) {
		UserAssertDetail domain = userAssertDetailRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());
		
		userAssertDetailRepo.save(domain);
	}

	@Override
	public UserAssertDetailDto findById(String id) {		
		UserAssertDetailDto modelDto = new UserAssertDetailDto();
		if(Validate.isString(id)){
			UserAssertDetail domain = userAssertDetailRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {

	}
	
}