package cs.service.expert;

import java.util.List;

import cs.domain.expert.Expert;
import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.repository.odata.ODataObj;

public interface ExpertService{

	public List<ExpertDto> findAll();
		
	PageModelDto<ExpertDto> get(ODataObj odataObj);

    String createExpert(ExpertDto expertDto);

	void deleteExpert(String id);

	void deleteExpert(String[] ids);
	
	void updateExpert(ExpertDto expertDto);
	
	Expert findExpertByName(String expertName);
	
	PageModelDto<ExpertDto> searchMuti(ExpertDto expertDto);
	 
	void updateAudit(String ids,String flag);

	public ExpertDto findById(String id);

	public List<ExpertDto> findAllRepeat();
		 
}
