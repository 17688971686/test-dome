package cs.service.expert;

import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertSelCondition;
import cs.domain.expert.ExpertSelCondition_;
import cs.model.PageModelDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.repository.odata.ODataObj;
import cs.repository.repositoryImpl.expert.ExpertSelConditionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 专家抽取条件 业务操作实现类
 * author: ldm
 * Date: 2017-5-23 19:49:58
 */
@Service
public class ExpertSelConditionServiceImpl  implements ExpertSelConditionService {


	private final ExpertSelConditionRepo expertSelConditionRepo;
	@Autowired
	private ICurrentUser currentUser;

	@Autowired
	public ExpertSelConditionServiceImpl(ExpertSelConditionRepo expertSelConditionRepo) {
		this.expertSelConditionRepo = expertSelConditionRepo;
	}

	@Override
	public PageModelDto<ExpertSelConditionDto> get(ODataObj odataObj) {
		PageModelDto<ExpertSelConditionDto> pageModelDto = new PageModelDto<ExpertSelConditionDto>();
		List<ExpertSelCondition> resultList = expertSelConditionRepo.findByOdata(odataObj);
		List<ExpertSelConditionDto> resultDtoList = new ArrayList<ExpertSelConditionDto>(resultList.size());
		
		if(resultList != null && resultList.size() > 0){
            resultList.forEach(x->{
				ExpertSelConditionDto modelDto = new ExpertSelConditionDto();
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
	public void save(ExpertSelConditionDto record) {
		ExpertSelCondition domain = new ExpertSelCondition(); 
		BeanCopierUtils.copyProperties(record, domain);
		expertSelConditionRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(ExpertSelConditionDto record) {
		ExpertSelCondition domain = expertSelConditionRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);
		expertSelConditionRepo.save(domain);
	}

	@Override
	public ExpertSelConditionDto findById(String id) {		
		ExpertSelConditionDto modelDto = new ExpertSelConditionDto();
		if(Validate.isString(id)){
			ExpertSelCondition domain = expertSelConditionRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {
        expertSelConditionRepo.deleteById(ExpertSelCondition_.id.getName(),id);
	}
	
}