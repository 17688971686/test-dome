package cs.service;

import java.util.List;

import cs.domain.Dict;
import cs.domain.Expert;
import cs.model.DictDto;
import cs.model.ExpertDto;
import cs.model.PageModelDto;
import cs.model.ProjectExpeDto;
import cs.model.WorkExpeDto;
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
	 
	 void updateAudit(String[] ids,String flag);
	 
}
