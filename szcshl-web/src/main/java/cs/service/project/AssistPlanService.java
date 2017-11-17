package cs.service.project;

import java.util.List;
import java.util.Map;

import cs.common.ResultMsg;
import cs.model.PageModelDto;
import cs.model.project.AssistPlanDto;
import cs.model.project.AssistUnitDto;
import cs.repository.odata.ODataObj;

/**
 * Description: 协审方案 业务操作接口
 * author: ldm
 * Date: 2017-6-6 14:48:31
 */
public interface AssistPlanService {
    
    PageModelDto<AssistPlanDto> get(ODataObj odataObj);

    ResultMsg save(AssistPlanDto record);

	void update(AssistPlanDto record);

	AssistPlanDto findById(String deptId);

	void delete(String id);

    Map<String,Object> initPlanManager();

    void cancelPlanSign(String planId, String signIds,boolean isMain);

	void saveLowPlanSign(AssistPlanDto assistPlanDto);

    AssistPlanDto getAssistPlanBySignId(String signId);


    /**
     * 保存抽签结果
     * @param planId
     * @param drawAssitUnitIds
     * @param unSelectedIds
     * @return
     */
    ResultMsg saveDrawAssistUnit(String planId,String drawAssitUnitIds, String unSelectedIds);
    
   void  updateDrawType(String id,String drawType);

    /**
     * 保存手动添加协审单位信息
     * @param planId
     * @param unitId
     */
   void addAssistUnit(String planId,String unitId);
   
   List<AssistUnitDto> getAssistUnit(String planId);

    /**
     * 保存计划信息和协审信息
     * @param record
     * @return
     */
    ResultMsg savePlanAndSign(AssistPlanDto record);
}
