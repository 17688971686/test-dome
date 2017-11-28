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

    /**
     * 保存协审项目信息
     * @param record
     * @return
     */
    ResultMsg save(AssistPlanDto record);

    /**
     * 根据主键查询
     * @param deptId
     * @return
     */
	AssistPlanDto findById(String deptId);

    /**
     * 删除操作
     * @param id
     */
	void delete(String id);

    /**
     * 协审计划管理页面
     * @return
     */
    Map<String,Object> initPlanManager(String isOnlySign);

    /**
     * 删除协审计划中的协审项目（所有）
     * @param planId
     * @param signIds
     * @param isMain
     */
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
    ResultMsg addAssistUnit(String planId,String unitId);
   
   List<AssistUnitDto> getAssistUnit(String planId);

    /**
     * 保存计划信息和协审信息
     * @param record
     * @return
     */
    ResultMsg savePlanAndSign(AssistPlanDto record);
}
