package cs.service.topic;

import cs.common.utils.BeanCopierUtils;
import cs.common.utils.SessionUtil;
import cs.common.utils.Validate;
import cs.domain.topic.TopicInfo;
import cs.model.PageModelDto;
import cs.model.topic.TopicInfoDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.topic.TopicInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
/**
 * Description: 课题研究 业务操作实现类
 * author: sjy
 * Date: 2017-9-4 15:04:55
 */
@Service
public class TopicInfoServiceImpl  implements TopicInfoService {

	@Autowired
	private TopicInfoRepo topicInfoRepo;
	
	@Override
	public PageModelDto<TopicInfoDto> get(ODataObj odataObj) {
		PageModelDto<TopicInfoDto> pageModelDto = new PageModelDto<TopicInfoDto>();
		List<TopicInfo> resultList = topicInfoRepo.findByOdata(odataObj);
		List<TopicInfoDto> resultDtoList = new ArrayList<TopicInfoDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				TopicInfoDto modelDto = new TopicInfoDto();
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
	public void save(TopicInfoDto record) {
		TopicInfo domain = new TopicInfo(); 
		BeanCopierUtils.copyProperties(record, domain); 
		Date now = new Date();
		domain.setCreatedBy(SessionUtil.getDisplayName());
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setCreatedDate(now);
		domain.setModifiedDate(now);
		topicInfoRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(TopicInfoDto record) {
		TopicInfo domain = topicInfoRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		domain.setModifiedBy(SessionUtil.getDisplayName());
		domain.setModifiedDate(new Date());
		
		topicInfoRepo.save(domain);
	}

	@Override
	public TopicInfoDto findById(String id) {		
		TopicInfoDto modelDto = new TopicInfoDto();
		if(Validate.isString(id)){
			TopicInfo domain = topicInfoRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {

	}
	
}