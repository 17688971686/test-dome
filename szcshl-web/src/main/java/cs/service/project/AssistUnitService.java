package cs.service.project;

import cs.model.PageModelDto;
import cs.model.project.AssistUnitDto;
import cs.model.project.AssistUnitUserDto;
import cs.repository.odata.ODataObj;


/**
 * Description: 协审单位 业务操作接口
 * author: ldm
 * Date: 2017-6-6 14:49:58
 */
public interface AssistUnitService {
    
    PageModelDto<AssistUnitDto> get(ODataObj odataObj);

	void save(AssistUnitDto record);

	void update(AssistUnitDto record);

	AssistUnitDto findById(String deptId);

	void delete(String id);
	
	PageModelDto<AssistUnitUserDto> getUnitAndUser(String id);
	
	PageModelDto<AssistUnitUserDto> getUserNotIn(ODataObj odataObj,String id);
	
	void addUser(String unitId,String userId);
	
	void removeUser(String unitId,String userId);
	
	void removeUsers(String unitId,String[] userIds);

}
