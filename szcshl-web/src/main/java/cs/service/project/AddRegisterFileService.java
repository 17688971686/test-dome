package cs.service.project;

import java.util.List;
import java.util.Map;

import cs.domain.project.AddRegisterFile;
import cs.model.PageModelDto;
import cs.model.project.AddRegisterFileDto;
import cs.model.project.AddSuppLetterDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 登记补充资料 业务操作接口
 * author: ldm
 * Date: 2017-8-3 15:26:51
 */
public interface AddRegisterFileService {
    
    PageModelDto<AddRegisterFileDto> get(ODataObj odataObj);

	void save(AddRegisterFileDto addRegisterFileDtos);

	void update(AddRegisterFileDto[] addRegisterFileDtos);

	AddRegisterFileDto findById(String deptId);

	void deleteRegisterFile(String id);
	
	 Map<String,Object> initprint( String signid);
	 
	 List<AddRegisterFileDto> findbySuppdate(String suppDate);

	Map<String, Object> initRegisterFile(String signid);

	List<AddRegisterFileDto> initRegisterFileData(ODataObj odataObj);

}
