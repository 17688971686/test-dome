package cs.service.project;

import cs.model.PageModelDto;
import cs.model.project.AssistUnitDto;
import cs.repository.odata.ODataObj;

import java.util.List;


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

	void delete(String ids);

	/**
	 * 选择抽签单位
	 * @param planId
	 * @param number
	 * @return
	 */
	List<AssistUnitDto> findDrawUnit(String planId,Integer number,String drawType);
	
	List<AssistUnitDto> getAssistUnitByPlanId(String planId);

}
