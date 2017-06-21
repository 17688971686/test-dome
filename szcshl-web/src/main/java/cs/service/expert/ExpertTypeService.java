package cs.service.expert;

import java.util.List;

import cs.domain.expert.ExpertType;
import cs.model.expert.ExpertTypeDto;
import cs.repository.odata.ODataObj;

public interface ExpertTypeService {
	
	void saveExpertType(ExpertTypeDto expertTypeDto);
	
	List<ExpertTypeDto> getExpertType(ODataObj odataObj);
	
	ExpertTypeDto getExpertTypeById(String expertTypeId);
	
	void updateExpertType(ExpertTypeDto expertTypeDto);
	
	void deleteExpertType(String ids);

}
