package cs.service.project;

import cs.common.ResultMsg;
import cs.model.project.AssistPlanSignDto;

import java.util.List;

/**
 * Description: 协审项目 业务操作接口
 * author: ldm
 * Date: 2017-6-6 14:50:37
 */
public interface AssistPlanSignService {
	List<AssistPlanSignDto> getPlanSignByPlanId(String planId);

	/**
	 * 保存填写的协审计划信息
	 * @param planSigns
	 * @return
	 */
	ResultMsg savePlanSign(AssistPlanSignDto[] planSigns);

	/**
	 * 根据项目ID，查询对应的协审信息
	 * @param signId
	 * @return
	 */
	List<AssistPlanSignDto> findBySignId(String signId);

	/**
	 * 通过收文Id 获取协审单位和协审费用
	 * @param signId
	 * @return
	 */
	ResultMsg findAssistPlanSignBySignId(String signId);


}
