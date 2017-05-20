package cs.service.project;

import java.util.List;
import java.util.Map;

import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;

public interface DispatchDocService {

	 void save(DispatchDocDto dispatchDocDto) throws Exception;
	 Map<String, Object> initDispatchData(String signId);
	 List<SignDto> getSign(String linkSignId);
	 List<SignDto> getSignbyIds(String[] ids);
	 void mergeDispa(String signId,String linkSignId);
	 Map<String,Object> getSeleSignBysId(String bussnessId);
	 String fileNum(String dispaId);

	 DispatchDocDto initDispatchBySignId(String signId);
}
