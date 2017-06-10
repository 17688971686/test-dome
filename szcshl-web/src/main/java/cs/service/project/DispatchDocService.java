package cs.service.project;

import java.util.List;
import java.util.Map;

import cs.domain.project.Sign;
import cs.model.project.DispatchDocDto;
import cs.model.project.SignDto;
import cs.repository.odata.ODataObj;

public interface DispatchDocService {

	 void save(DispatchDocDto dispatchDocDto) throws Exception;
	 
	 Map<String, Object> initDispatchData(String signId);
	 
	 List<SignDto> getSign(SignDto signDto);
	 
	 List<SignDto> getSignbyIds(String[] ids);
	 
	 void mergeDispa(String signId,String linkSignId);
	 
	 Map<String,Object> getSeleSignBysId(String bussnessId);
	 
	 String fileNum(String dispaId);

	 DispatchDocDto initDispatchBySignId(String signId);
	 
	 void deleteMergeDispa(String dispathId);
	 
	 String getRelatedFileNum(String dispaId);
}
