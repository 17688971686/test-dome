package cs.service.topic;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.topic.Filing;
import cs.model.PageModelDto;
import cs.model.topic.FilingDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.topic.FilingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description: 课题归档 业务操作实现类
 * author: ldm
 * Date: 2017-9-4 15:40:48
 */
@Service
public class FilingServiceImpl  implements FilingService {

	@Autowired
	private FilingRepo filingRepo;
	
	@Override
	public PageModelDto<FilingDto> get(ODataObj odataObj) {
		PageModelDto<FilingDto> pageModelDto = new PageModelDto<FilingDto>();
		List<Filing> resultList = filingRepo.findByOdata(odataObj);
		List<FilingDto> resultDtoList = new ArrayList<FilingDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				FilingDto modelDto = new FilingDto();
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
	public void save(FilingDto record) {
		Filing domain = new Filing(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		filingRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(FilingDto record) {
		Filing domain = filingRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());
		
		filingRepo.save(domain);
	}

	@Override
	public FilingDto findById(String id) {		
		FilingDto modelDto = new FilingDto();
		if(Validate.isString(id)){
			Filing domain = filingRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {

	}
	
}