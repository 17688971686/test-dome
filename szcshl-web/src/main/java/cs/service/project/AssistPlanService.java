package cs.service.project;

import cs.model.PageModelDto;
import cs.model.project.AssistPlanDto;
import cs.repository.odata.ODataObj;

import java.util.Map;

/**
 * Description: 协审方案 业务操作接口
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
public interface AssistPlanService {
    
    PageModelDto<AssistPlanDto> get(ODataObj odataObj);

	void save(AssistPlanDto record);

	void update(AssistPlanDto record);

	AssistPlanDto findById(String deptId);

	void delete(String id);

    Map<String,Object> initPlanManager();

    void cancelPlanSign(String planId, String signIds,boolean isMain);

	void saveLowPlanSign(AssistPlanDto assistPlanDto);
}
