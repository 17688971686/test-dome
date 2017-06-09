package cs.service.project;

import cs.model.PageModelDto;
import cs.model.project.AssistUnitUserDto;
import cs.repository.odata.ODataObj;


/**
 * Description: 协审单位用户 业务操作接口
 * author: ldm
 * Date: 2017-6-9 9:37:54
 */
public interface AssistUnitUserService {
    
    PageModelDto<AssistUnitUserDto> get(ODataObj odataObj);

	void save(AssistUnitUserDto record);

	void update(AssistUnitUserDto record);

	AssistUnitUserDto findById(String deptId);

	void delete(String id);

}
