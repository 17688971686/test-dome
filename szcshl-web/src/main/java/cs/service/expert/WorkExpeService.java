package cs.service.expert;

import cs.common.ResultMsg;
import cs.model.expert.WorkExpeDto;
import cs.repository.odata.ODataObj;

import java.util.List;

public interface WorkExpeService {
	 List<WorkExpeDto> getWork(ODataObj odataObj);
	 void deleteWork(String ids);
	 void updateWork(WorkExpeDto workExpeDto);
	ResultMsg saveWorkExpe(WorkExpeDto work);
}
