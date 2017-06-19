package cs.service.expert;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cs.common.ICurrentUser;
import cs.common.utils.BeanCopierUtils;
import cs.domain.expert.Expert;
import cs.domain.expert.ExpertType;
import cs.model.expert.ExpertTypeDto;
import cs.repository.repositoryImpl.expert.ExpertTypeRepo;

@Service
public class ExpertTypeServiceImpl implements ExpertTypeService{
	
	@Autowired
	private ICurrentUser currentUser;
	
	@Autowired
	private ExpertTypeRepo expertTypeRepo;

	@Override
	@Transactional
	public void saveExpertType(List<ExpertTypeDto> expertTypeDtoList, String expertId) {
		
		Expert expert=new Expert();
		expert.setExpertID(expertId);
		for(ExpertTypeDto expertTypeDto:expertTypeDtoList){
			
			ExpertType expertType=new ExpertType();
			if(expertTypeDto!=null){
				
				
				BeanCopierUtils.copyProperties(expertTypeDto, expertType);
			}
			
			
			expertType.setId(UUID.randomUUID().toString());
			expertType.setCreatedBy(currentUser.getDisplayName());
			
			expertType.setCreatedDate(new Date());
			expertType.setModifiedBy(currentUser.getDisplayName());
			expertType.setModifiedDate(new Date());
			expertType.setExpert(expert);
			
			expertTypeRepo.save(expertType);
			
		}
		
	}
	

}
