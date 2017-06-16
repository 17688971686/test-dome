package cs.service.expert;

import java.util.List;

import cs.model.PageModelDto;
import cs.model.expert.ExpertDto;
import cs.model.expert.ExpertSelConditionDto;
import cs.repository.odata.ODataObj;

public interface ExpertService{
		
	PageModelDto<ExpertDto> get(ODataObj odataObj);

    String createExpert(ExpertDto expertDto);

	void deleteExpert(String id);

	void deleteExpert(String[] ids);
	
	void updateExpert(ExpertDto expertDto);
	 
	void updateAudit(String ids,String flag);

	public ExpertDto findById(String id);

	public List<ExpertDto> findAllRepeat();

	List<ExpertDto> findExpert(String workprogramId,String reviewId,ExpertSelConditionDto[] epSelCondition);

    Integer countExpert(String workprogramId, String reviewId,ExpertSelConditionDto epSelCondition);

    void savePhone(byte[] bytes, String expertId);

	byte[] findExpertPhoto(String expertId);
}
