package cs.service.expert;

import java.util.List;

import cs.model.expert.ExpertTypeDto;

public interface ExpertTypeService {
	
	void saveExpertType(List<ExpertTypeDto> expertTypeDtoList,String expertId);

}
