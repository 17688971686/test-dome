package cs.service.project;

import java.util.Map;

import cs.model.project.DispatchDocDto;

public interface DispatchDocService {

	void save(DispatchDocDto dispatchDocDto) throws Exception;
	 Map<String, Object> initDispatchData(String signId);

}
