package cs.service.expert;

import cs.common.HqlBuilder;
import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.common.utils.Validate;
import cs.domain.expert.ExpertSelected;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertSelectedDto;
import cs.repository.repositoryImpl.expert.ExpertSelectedRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 抽取专家 业务操作实现类
 * author: ldm
 * Date: 2017-6-14 14:26:49
 */
@Service
public class ExpertSelectedServiceImpl  implements ExpertSelectedService {

	@Autowired
	private ExpertSelectedRepo expertSelectedRepo;
	@Autowired
	private ICurrentUser currentUser;

	@Override
	@Transactional
	public void save(ExpertSelectedDto record) {
		ExpertSelected domain = new ExpertSelected(); 
		BeanCopierUtils.copyProperties(record, domain); 

		expertSelectedRepo.save(domain);
	}

	@Override
	@Transactional
	public void update(ExpertSelectedDto record) {
		ExpertSelected domain = expertSelectedRepo.findById(record.getId());
		BeanCopierUtils.copyPropertiesIgnoreNull(record, domain);

		expertSelectedRepo.save(domain);
	}

	@Override
	public ExpertSelectedDto findById(String id) {		
		ExpertSelectedDto modelDto = new ExpertSelectedDto();
		if(Validate.isString(id)){
			ExpertSelected domain = expertSelectedRepo.findById(id);
			BeanCopierUtils.copyProperties(domain, modelDto);
		}		
		return modelDto;
	}

	@Override
	@Transactional
	public void delete(String id) {

	}

}